package net.alphaatom.repeaterer;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Diode;

public class ChangeBlockTask implements Runnable {
	
	Block blockToChange;
	int delay;

	public ChangeBlockTask(Block block, int _delay) {
		blockToChange = block;
		delay = _delay;
	}

	@Override
	public void run() {
		BlockState state = blockToChange.getState();
		Diode diode = (Diode) state.getData();
		diode.setDelay(delay);
		state.setData(diode);
		state.update(true);
	}

}
