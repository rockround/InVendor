package ServerSide;

public class FrameObject {
int lifeTime;
boolean inactive = false;
int goodVibes= 0;
int targetVibes = 0;
long birthTime = 0;
String name;
public FrameObject(String name, long birthTime, int lifeTime, int targetVibes){
	this.lifeTime = lifeTime;
	this.targetVibes = targetVibes;
	this.name = name;
	this.birthTime = birthTime;
}
boolean refresh(){
	if(lifeTime<=System.currentTimeMillis()-birthTime){
		inactive = true;
	}
	return inactive;
}
public void deactivate(){
	inactive = true;
}
public boolean isDead(){
	return inactive;
}
public void setTarget(int targetVal){
	targetVibes = targetVal;
}
}
