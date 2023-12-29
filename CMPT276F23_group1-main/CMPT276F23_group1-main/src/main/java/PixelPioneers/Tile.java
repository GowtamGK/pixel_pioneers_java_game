package PixelPioneers;

class Tile  
{
	//variables possible for a tile
	private int topWall;
	private int bottomWall;
	private int leftWall;
	private int rightWall;
	private int hasPellet;
	private int penalty;
	private int endTile;
	private int bonusReward;
	private int voidTile;
	private int pelletCollected;
	
    public Tile() { //constructor
		//default no tile attributes
		topWall = 0;
		bottomWall = 0;
		leftWall = 0;
		rightWall = 0;
		hasPellet = 0;
		penalty = 0;
		endTile = 0;
		bonusReward = 0;
		voidTile = 0;
		pelletCollected = 0;
    }

	//setters
	public void setTopWall(int top) {
		topWall = top;
	}
	public void setBottomWall(int bottom) {
		bottomWall = bottom;
	}
	public void setRightWall(int right) {
		rightWall = right;
	}
	public void setLeftWall(int left) {
		leftWall = left;
	}
	public void setPellet(int pellet) {
		hasPellet = pellet;
	}
	public void setPenalty(int pen) {
		penalty = pen;
	}
	public void setEndTile(int end) {
		endTile = end;
	}
	public void setBonusReward(int bonus) {
		bonusReward = bonus;
	}
	public void setVoidTile(int voidT) {
		voidTile = voidT;
	}
	public void setPelletCollected(int pelCol) {
		pelletCollected = pelCol;
	}
	
	//getters
	public int getTopWall() {
		return topWall;
	}

	public int getBottomWall() {
		return bottomWall;
	}

	public int getLeftWall() {
		return leftWall;
	}

	public int getRightWall() {
		return rightWall;
	}

	public int getPellet() {
		return hasPellet;
	}

	public int getPenalty() {
		return penalty;
	}

	public int getEndTile() {
		return endTile;
	}

	public int getBonusReward() {
		return bonusReward;
	}

	public int getVoidTile() {
		return voidTile;
	}

	public int getPelletCollected() {
		return pelletCollected;
	}
}