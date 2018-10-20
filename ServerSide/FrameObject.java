
public class FrameObject {
int lifeTime;
boolean inactive = false;
int goodVibes= 0;
int targetVibes = 0;
long birthTime = 0;
public FrameObject(long birthTime, int lifeTime, int targetVibes){
	this.lifeTime = lifeTime;
	this.targetVibes = targetVibes;
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
