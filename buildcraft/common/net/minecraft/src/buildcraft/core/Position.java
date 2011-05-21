package net.minecraft.src.buildcraft.core;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class Position {
	// FIXME: double is probably not the way to go here - we may have two 
	// versions of this, for double and for int
	public double i, j, k;
	public Orientations orientation;
	
	public Position (double ci, double cj, double ck) {
		i = ci;
		j = cj;
		k = ck;
		orientation = Orientations.Unknown;
	}
	
	public Position (double ci, double cj, double ck, Orientations corientation) {
		i = ci;
		j = cj;
		k = ck;
		orientation = corientation;
	}
	
	public Position (Position p) {
		i = p.i;
		j = p.j;
		k = p.k;
		orientation = p.orientation;
	}
	
	public Position (NBTTagCompound nbttagcompound) {
		i = nbttagcompound.getDouble("i");
		j = nbttagcompound.getDouble("j");
		k = nbttagcompound.getDouble("k");
		
		orientation = Orientations.Unknown;
	}
	
	public Position (TileEntity tile) {
		i = tile.xCoord;
		j = tile.yCoord;
		k = tile.zCoord;
	}
	
	public void moveRight (double step) {
		switch (orientation) {
		case ZPos:
			i = i - step;
			break;
		case ZNeg:
			i = i + step;    			
			break;
		case XPos:
			k = k + step;
			break;
		case XNeg:
			k = k - step;
			break;
		}
	}
	
	public void moveLeft (double step) {
		moveRight(-step);
	}
	
	public void moveForwards (double step) {
		switch (orientation) {
		case YPos:
			j = j + step;
			break;
		case YNeg:
			j = j - step;
			break;
		case ZPos:
			k = k + step;
			break;
		case ZNeg:
			k = k - step;	
			break;
		case XPos:
			i = i + step;
			break;		
		case XNeg:
			i = i - step;
			break;
		}
	}	
	
	public void moveBackwards (double step) {
		moveForwards(-step);
	}
	
	public void moveUp (double step) {
		switch (orientation) {
		case ZPos: case ZNeg: case XPos: case XNeg:
			j = j + step;
			break;
		}
		
	}
	
	public void moveDown (double step) {
		moveUp (-step);
	}
	
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setDouble("i", i);
		nbttagcompound.setDouble("j", j);
		nbttagcompound.setDouble("k", k);
	}
	
}