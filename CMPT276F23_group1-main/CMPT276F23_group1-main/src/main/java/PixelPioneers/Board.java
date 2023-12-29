package PixelPioneers;

class Board  
{
    //tile array to keep info
	private Tile[] mazeData;

	public Board(int descWidth, int descHeight, String[] mazeDesc) {
		int idx = 0;
		mazeData = new Tile[descWidth * descHeight];
		for (int y = 0; y < descHeight; y++) {
			for (int x = 0; x < descWidth; x++) {
				mazeData[idx] = new Tile();
				char ch = mazeDesc[y].charAt(x);
				// Parse the maze description
				if (ch == 'X') {
					mazeData[idx].setVoidTile(1);
				} else {
					// Set wall properties
					if (y > 0 && mazeDesc[y-1].charAt(x) == 'X') {
						mazeData[idx].setTopWall(1);
					}
					if (y < descHeight - 1 && mazeDesc[y+1].charAt(x) == 'X') {
						mazeData[idx].setBottomWall(1);
					}
					if (x > 0 && mazeDesc[y].charAt(x-1) == 'X') {
						mazeData[idx].setLeftWall(1);
					}
					if (x < descWidth - 1 && mazeDesc[y].charAt(x+1) == 'X') {
						mazeData[idx].setRightWall(1);
					}
					// Set other properties based on the character
					switch (ch) {
						case 'P':
							mazeData[idx].setPenalty(1);
							break;
						case ' ':
							mazeData[idx].setPellet(1);
							mazeData[idx].setPelletCollected(1);
							break;
						case 'E': // This checks for the end tile
							mazeData[idx].setEndTile(1);
							break;
						case 'B': // This checks for the bonus reward
							mazeData[idx].setBonusReward(1);
							break;
					}
				}
				idx++;
			}
		}
	}
	//Getter: Method to get a specific tile by index
	public Tile getTile(int idx) {
		if (idx >= 0 && idx < mazeData.length) {
			return mazeData[idx];
		} else {
			// Handle the case where idx is out of bounds, or throw an exception
			return null;
		}
	}
}