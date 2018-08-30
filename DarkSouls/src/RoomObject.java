import java.util.ArrayList;
import java.util.List;

public class RoomObject
{
	public mainGame.Room currentRoom;
	public mainGame.Room nextRoom;
	public mainGame.Room previousRoom;
	public mainGame.Room leftRoom;
	public mainGame.Room rightRoom;
	public String description;
	public String lookAround;
	public boolean bonfire;
	public List <ItemObject> items = new ArrayList<ItemObject>();
	public mainGame.Item requiredItem;
	public List <Enemy> enemies = new ArrayList<Enemy>();
	
	public static RoomObject AddRoom(mainGame.Room currentRoomToAdd, mainGame.Room nextRoomToAdd, mainGame.Room previousRoomToAdd, mainGame.Room leftRoomToAdd, mainGame.Room rightRoomToAdd, String descriptionToAdd, String lookAroundToAdd, boolean bonfireToAdd, List <ItemObject> itemsToAdd, mainGame.Item requiredItemToAdd, List <Enemy> enemiesToAdd)
	{
		RoomObject roomObject = new RoomObject();
		roomObject.currentRoom = currentRoomToAdd;
		roomObject.nextRoom = nextRoomToAdd;
		roomObject.previousRoom = previousRoomToAdd;
		roomObject.leftRoom = leftRoomToAdd;
		roomObject.rightRoom = rightRoomToAdd;
		roomObject.requiredItem = requiredItemToAdd;
		roomObject.description = descriptionToAdd;
		roomObject.lookAround = lookAroundToAdd;
		roomObject.bonfire = bonfireToAdd;
		roomObject.items = itemsToAdd;
		roomObject.enemies = enemiesToAdd;
		return roomObject;
	}
}
