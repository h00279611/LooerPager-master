package com.example.annotation.process;

import com.example.annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {"com.example.annotation.BindView"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({"content", "test"})
public class InjectorProcessor extends AbstractProcessor {
    private static final String GEN_CLASS_SUFFIX = "$Injector";
    private static String injectorName = "ViewInjector";
    private static String packageName = "com.annotation";

    private Types mTypeUtils;
    private Elements mElementUtils;
    // 文件生成器
    private Filer mFiler;

    // 用于输出警告、错误等日志
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

        // 从App的build.gradle中定义中获取值
        Map<String, String> options = processingEnvironment.getOptions();
        String userPackageName = options.get("packageName");
        if (userPackageName != null) {
            packageName = userPackageName;
        }

        String userInjectorName = options.get("className");
        if (userInjectorName != null) {
            injectorName = userInjectorName;
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "packageName:" + packageName + ", className:" + injectorName);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        //process会被调用三次，只有一次是可以处理InjectView注解的，原因不明
        if (elements.size() == 0) {
            return true;
        }

        Map<Element, List<Element>> elementMap = new HashMap<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("package " + packageName + ";\n")
                .append("public class " + injectorName + " {\n");

        //遍历所有被InjectView注释的元素
        for (Element element : elements) {
            //如果标注的对象不是FIELD则报错,这个错误其实不会发生因为InjectView的Target已经声明为ElementType.FIELD了
            if (element.getKind() != ElementKind.FIELD) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "is not a FIELD", element);
            }

            //这里可以先将element转换为VariableElement,但我们这里不需要
            //VariableElement variableElement = (VariableElement) element;

            //如果不是View的子类则报错
            if (!isView(element.asType())) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "is not a View", element);
            }

            //获取所在类的信息
            Element clazz = element.getEnclosingElement();

            //按类存入map中
            addElement(elementMap, clazz, element);
        }


        buffer.append("\tpublic static void inject(android.app.Activity arg) {\n");

        for (Map.Entry<Element, List<Element>> entry : elementMap.entrySet()) {
            Element clazz = entry.getKey();

            //获取类名
            String className = clazz.getSimpleName().toString();

            //获取所在的包名
            String packageName = mElementUtils.getPackageOf(clazz).asType().toString();

            //生成注入代码
            generateInjectorCode(packageName, className, entry.getValue());

            //完整类名
            String fullName = clazz.asType().toString();


            buffer.append("if(arg instanceof " + fullName +") {\n")
                    .append("\t\t" + fullName + GEN_CLASS_SUFFIX + ".inject(arg);\n")
                    .append("return;\n")
                    .append("\t}\n");
        }

        buffer.append("}");

        generateCode(injectorName, buffer.toString());

        return true;
    }

    //递归判断android.view.View是不是其父类
    private boolean isView(TypeMirror type) {
        List<? extends TypeMirror> supers = mTypeUtils.directSupertypes(type);
        if (supers.size() == 0) {
            return false;
        }
        for (TypeMirror superType : supers) {
            if (superType.toString().equals("android.view.View") || isView(superType)) {
                return true;
            }
        }
        return false;
    }

    private void addElement(Map<Element, List<Element>> map, Element clazz, Element field) {
        List<Element> list = map.get(clazz);
        if (list == null) {
            list = new ArrayList<>();
            map.put(clazz, list);
        }
        list.add(field);
    }

    private void generateCode(String className, String code) {
        try {
            JavaFileObject file = mFiler.createSourceFile(className);
            Writer writer = file.openWriter();
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成注入代码
     *
     * @param packageName 包名
     * @param className   类名
     * @param views       需要注入的成员变量
     */
    private void generateInjectorCode(String packageName, String className, List<Element> views) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + packageName + ";\n\n")
                .append("public class " + className + GEN_CLASS_SUFFIX + " {\n")
                .append("\tpublic static void inject(" + className + " arg) {\n");

        for (Element element : views) {
            //获取变量类型
            String type = element.asType().toString();

            //获取变量名
            String name = element.getSimpleName().toString();

            //id
            int resourceId = element.getAnnotation(BindView.class).value();

            builder.append("\t\targ." + name + "=(" + type + ")arg.findViewById(" + resourceId + ");\n");
        }

        builder.append("\t}\n")
                .append("}");

        //生成代码
        generateCode(className + GEN_CLASS_SUFFIX, builder.toString());
    }
}

