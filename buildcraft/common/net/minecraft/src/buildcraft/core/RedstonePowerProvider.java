package net.minecraft.src.buildcraft.core;

import net.minecraft.src.BuildCraftCore;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class RedstonePowerProvider extends PowerProvider {
	
	private boolean lastPower = false;
	
	public RedstonePowerProvider () {
		this.powerLoss = 0;
		this.powerLossRegularity = 0;
	}
	
	@Override
	public boolean preConditions(IPowerReceptor receptor) {
		TileEntity tile = (TileEntity) receptor;
		
		boolean currentPower = tile.worldObj.isBlockIndirectlyGettingPowered(
				tile.xCoord, tile.yCoord, tile.zCoord);
		
		if (BuildCraftCore.continuousCurrentModel) {
			if (currentPower) {
				return true;
			}
		} else {			
			if (currentPower != lastPower) {
				lastPower = currentPower;

				if (currentPower) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public int useEnergy (int min, int max, boolean doUse) {		
		return min;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		lastPower = nbttagcompound.getBoolean("lastPower");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		nbttagcompound.setBoolean("lastPower", lastPower);
	}

	public void configure(int latency, int minEnergyReceived,
			int maxEnergyReceived, int minActivationEnergy, int maxStoredEnergy) {
		super.configure(latency, minEnergyReceived, maxEnergyReceived,
				minActivationEnergy, maxStoredEnergy);

		this.minActivationEnergy = 0;
		this.energyStored = 1;
	}
	
	@Override
	public void configurePowerPerdition(int powerLoss, int powerLossRegularity) {
		
	}
}
