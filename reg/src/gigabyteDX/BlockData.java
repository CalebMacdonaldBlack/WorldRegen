package gigabyteDX;

import java.io.Serializable;

public class BlockData implements Serializable{
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    
	String material;
	Byte data;
	Long date;
	int x,y,z;
	String world; 
	
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public Byte getData() {
		return data;
	}
	public BlockData(String world, String material, Byte data, Long date, int x, int y, int z) {
		super();
		this.material = material;
		this.data = data;
		this.date = date;
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}
	public void setData(Byte data) {
		this.data = data;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	
}
