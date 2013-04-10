package ru.mipt.sign.facade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.forex.Bar;
import ru.mipt.sign.learn.LearningCenter;
import ru.mipt.sign.neurons.NeuroNet;

public class NeuroManager
{

	public void start(ApplicationContext appCtx) throws NeuronNotFound
	{
		NeuroNet nn = appCtx.getNet();
		List<Bar> in = appCtx.getData();
		List<Double> prevResult = new ArrayList<Double>();
		System.out.println("Start processing bars: \n");
		for (Iterator<Bar> it = in.iterator(); it.hasNext();)
		{
			Bar curr = it.next();
			nn.nextInput(curr.getList());
			nn.calc();
			learn(nn, prevResult);
			prevResult = nn.getResult();
			System.out.println(curr.toString() + "  Result: " + prevResult.toString());
		}
		System.out.println("\nStop processing bars.");
	}

	public void learn(NeuroNet nn, List rightValue) throws NeuronNotFound
	{
		if (rightValue.size() == 0)
			return;
		LearningCenter lc = new LearningCenter();
		Random rnd = new Random();
		Integer type = rnd.nextInt(100);
		if (type < 50)
		{
			lc.learn(nn, rightValue, lc.TYPE_CREATE);
			System.out.println("create");
		}
		else if (type < 80)
		{
			lc.learn(nn, rightValue, lc.TYPE_CONNECT);
			System.out.println("connect");
		}
		else
		{
			lc.learn(nn, rightValue, lc.TYPE_DISCONNECT);
			System.out.println("disconnect");
		}
	}
}
