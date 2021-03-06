package wuye.bean;

public class PianquData {
	
	private int pianquid;
	private int paiming;
	private double score;
	private String pianquName;
	private int streetid;
	private String streetName;
	private int areaid;
	private String areaName;
	private double neiscore;
	private double waiscore;
	private double neidel;
	private double waidel;
	
	
	public double getNeidel() {
		return neidel;
	}
	public double getWaidel() {
		return waidel;
	}
	public double getNeiscore() {
		return neiscore;
	}
	public void setNeiscore(double neiscore) {
		this.neiscore = 100 - neiscore;
		this.neidel = neiscore;
	}
	public double getWaiscore() {
		return waiscore;
	}
	public void setWaiscore(double waiscore) {
		this.waiscore = 100-waiscore;
		this.waidel = waiscore;
	}
	public int getPianquid() {
		return pianquid;
	}
	public void setPianquid(int pianquid) {
		this.pianquid = pianquid;
	}
	public int getPaiming() {
		return paiming;
	}
	public void setPaiming(int paiming) {
		this.paiming = paiming;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getPianquName() {
		return pianquName;
	}
	public void setPianquName(String pianquName) {
		this.pianquName = pianquName;
	}
	public int getStreetid() {
		return streetid;
	}
	public void setStreetid(int streetid) {
		this.streetid = streetid;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public int getAreaid() {
		return areaid;
	}
	public void setAreaid(int areaid) {
		this.areaid = areaid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	
}
