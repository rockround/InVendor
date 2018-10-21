

public class FrameObject {
int lifeTime;
boolean inactive = false;
boolean funded = false;
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
	}else{
		if(goodVibes == targetVibes){
			funded = inactive = true;
		}
	}
	return inactive;
}
public boolean isFunded(){
	return funded;
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
public void addVibes(int vibes){
	goodVibes+=vibes;
}
}
