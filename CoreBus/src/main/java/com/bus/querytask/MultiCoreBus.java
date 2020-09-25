package com.bus.querytask;

public class MultiCoreBus {

	public static final int RoundRobin = 1;// 轮转法
	public CoreBus[] buss;
	public int count = -1;
	public int coreNum = 1;

	public MultiCoreBus(int coreNum) {
		this.coreNum = coreNum;
		buss = new CoreBus[coreNum];
		for (int i = 0; i < buss.length; i++) {
			buss[i] = new CoreBus("CPU" + i);
		}
	}

	/**
	 * 采用轮转法得到CPU总线
	 * 
	 * @return
	 */
	public CoreBus getCoreBusByRoundRobin() {
		if (count == coreNum) {
			count = 0;
		}
		CoreBus bus = buss[count];
		count++;
		return bus;
	}
}
