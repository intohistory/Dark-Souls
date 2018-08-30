import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class mainGame 
{
	enum Item{CellKey, BasicShield, StarterSword, LongSword}
	enum Room{Cell, HollowHallway, LadderRoom, Bonfire1, BossCourtyard, Bonfire2, ArcherHallway, BoulderRoom, UpperFloor, Path}
	
	public static int mattGayLevel = 100;
	
	
	public static ItemObject weaponSlot = null;
	public static ItemObject offhandSlot = null;
	public static ItemObject helmetSlot = null;
	public static ItemObject chestSlot = null;
	public static ItemObject leggingsSlot = null;
	public static ItemObject bootsSlot = null;
	
	public static RoomObject currentRoom;
	public static RoomObject respawnRoom;
	public static boolean gameRunning = true;
	public static List <ItemObject> inventory = new ArrayList<ItemObject>();
	public static List <RoomObject> roomList = new ArrayList<RoomObject>();
	
	public static int currentPlayerHealth = 250;
	public static int maxPlayerHealth = 250;
	public static int playerDefense = 45;
	public static int playerDamage = 100;
	public static int playerAGI = 35;
	
	public static boolean isDodging = false;
	public static boolean isBlocking = false;
	public static boolean isHeavyAttacking = false;
	
	public static int currentSouls = 0;
	public static int soulsToLevelUp = 150;

	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		CreateRooms();
		respawnRoom = FindRoom(Room.Cell);
		Instructions();
		System.out.println("Press enter to start");
		if(scan.nextLine() != null)
		{
			MoveRooms(FindRoom(Room.Cell));
			Update();
		}
		scan.close();
	}
	public static void MoveRooms(RoomObject room)
	{
		currentRoom = room;
		System.out.println(room.description);
		if(CheckForEnemies(room))
		{
			PrintEnemyStats(room);
		}
	}
	public static RoomObject FindRoom(Room destination)
	{
		for(int x = 0; x < roomList.size(); x++)
		{
			RoomObject room = roomList.get(x);
			if(room.currentRoom == destination)
			{
				return room;
			}
		}
		System.out.println("Could not find that room, returning current room");
		return currentRoom;
	}
	
	public static void CreateRooms()
	{
		List<ItemObject> cellItemList = new ArrayList<ItemObject>();
		cellItemList.add(ItemObject.CreateItem(0, 0, 0, 0, 0, 0, Item.CellKey, ItemObject.equipSlot.none));
		String cellDescription = "You are in a dark, square cell\nSomeone has dropped a corpse into the cell from a hole in the ceiling";
		String cellLookAround = "In front of you is a cell door.\nNext to the cell door is a corpse.";
		roomList.add(RoomObject.AddRoom(Room.Cell, Room.HollowHallway, null, null, null, cellDescription, cellLookAround, false, cellItemList, Item.CellKey, null));
		
		List<Enemy> hallowHallwayEnemyList = new ArrayList<Enemy>();
		hallowHallwayEnemyList.add(Enemy.CreateEnemy(Enemy.EnemyTypes.Hollow));
		hallowHallwayEnemyList.add(Enemy.CreateEnemy(Enemy.EnemyTypes.Hollow));
		String hallowHallwayDescription = "You enter a dark hallway";
		String hallowHallwayLookAround = "At the end of the hallway is a ladder";
	   	roomList.add(RoomObject.AddRoom(Room.HollowHallway, Room.LadderRoom, Room.Cell, null, null, hallowHallwayDescription, hallowHallwayLookAround, false, null, null, hallowHallwayEnemyList));
	   	
	   	String ladderRoomDescription = "You enter a small room with a ladder in the middle";
	   	String ladderRoomLookAround = "You are in an empty room with only a ladder in it. You might want to climb up it.";
	   	roomList.add(RoomObject.AddRoom(Room.LadderRoom, Room.Bonfire1, Room.HollowHallway, null, null, ladderRoomDescription, ladderRoomLookAround, false, null, null, null));
	   	
	   	String bonfire1Description = "You enter a courtyard with a bonfire in the middle";
	   	String bonfire1LookAround = "You are in a room with a bonfire in the middle. The bonfire can be used to save your progress and restore your health";
	   	roomList.add(RoomObject.AddRoom(Room.Bonfire1, Room.BossCourtyard, Room.LadderRoom, null, null, bonfire1Description, bonfire1LookAround, true, null, null, null));
	   	
	   	List<Enemy> bossCourtyardEnemies = new ArrayList<Enemy>();
	   	bossCourtyardEnemies.add(Enemy.CreateEnemy(Enemy.EnemyTypes.AsylumDemonBoss));
	   	String bossCourtyardDescription = "You enter a large courtyard with a balcony above you and multiple doors.";
	   	String bossCourtyardLookAround = "You notice there is debris scatter throughout the courtyard. There is a door leading downwards to the left and a locked door in front of you.";
	   	roomList.add(RoomObject.AddRoom(Room.BossCourtyard, null, Room.Bonfire1, Room.Bonfire2, null, bossCourtyardDescription, bossCourtyardLookAround, false, null, null, bossCourtyardEnemies));
	   	
	   	String bonfire2Description = "You are in a very small damp room with a open doorway to your left";
	   	String bonfire2LookAround = "In the center of the room is another bonfire";
	   	roomList.add(RoomObject.AddRoom(Room.Bonfire2, null, null, Room.ArcherHallway, Room.BossCourtyard, bonfire2Description, bonfire2LookAround, true, null, null, null));
	   	
		List<ItemObject> archerHallwayItemList = new ArrayList<ItemObject>();
		archerHallwayItemList.add(ItemObject.CreateItem(15, 10, 0, 0, 0, 0, Item.BasicShield, ItemObject.equipSlot.offhand));
		archerHallwayItemList.add(ItemObject.CreateItem(0, 10, 0, 35, 0, 0, Item.LongSword, ItemObject.equipSlot.weapon));
		List<Enemy> archerHallwayEnemyList = new ArrayList<Enemy>();
		archerHallwayEnemyList.add(Enemy.CreateEnemy(Enemy.EnemyTypes.Archer));
	   	String archerHallwayDescription = "You enter a long hallway with an open cell to the left";
	   	String archerHallwayLookAround = "You are in a long hallway. There are many cells to the right and left, but you are only able to enter one. The open cell seems to contain a corpse";
	   	roomList.add(RoomObject.AddRoom(Room.ArcherHallway, Room.BoulderRoom, Room.Bonfire2, null, null, archerHallwayDescription, archerHallwayLookAround, false, archerHallwayItemList, null, archerHallwayEnemyList));
	   	
		List<Enemy> boulderRoomEnemyList = new ArrayList<Enemy>();
		boulderRoomEnemyList.add(Enemy.CreateEnemy(Enemy.EnemyTypes.RollingBoulder));
	   	String boulderRoomDescription = "You enter a small room with a staircase in front of you";
	   	String boulderRoomLookAround = "You are in a small room with a staircase directly in front of you. There is a large object on the top of the stairs";
	   	roomList.add(RoomObject.AddRoom(Room.BoulderRoom, Room.UpperFloor, Room.ArcherHallway, null, null, boulderRoomDescription, boulderRoomLookAround, false, null, null, boulderRoomEnemyList));
	   	
	   	
	}
	
	public static void Update()
	{
		if(gameRunning == true)
		{
			Scanner scan = new Scanner(System.in);
			String nextCommand = scan.nextLine();
			//If there are enemies in the room only accept battle commands
			if(CheckForEnemies(currentRoom))
			{
				boolean isFleeing = false;
				boolean validCommand = true;
				if(nextCommand.length() >= 8 && nextCommand.substring(0, 6).toLowerCase().equals("attack"))
				{
					Random rand = new Random();
					if(nextCommand.substring(7, 8) != null)
					{
						int enemyNum = Integer.parseInt(nextCommand.substring(7, 8));
						if(currentRoom.enemies.get(enemyNum).currentHealth > 0)
						{
							int damage = rand.nextInt(21) - 10 + playerDamage;
							currentRoom.enemies.get(enemyNum).currentHealth -= damage;
							System.out.println("Dealt "+damage+" to enemy "+enemyNum);
							if(currentRoom.enemies.get(enemyNum).currentHealth <= 0)
							{
								System.out.println("Killed enemy "+enemyNum);
								System.out.println("Gained "+currentRoom.enemies.get(enemyNum).soulReward+" souls");
								currentSouls += currentRoom.enemies.get(enemyNum).soulReward;
							}
						}
					}
				}
				else if(nextCommand.length() >= 13 && nextCommand.substring(0, 11).toLowerCase().equals("heavyattack"))
				{
					Random rand = new Random();
					if(nextCommand.substring(7, 8) != null)
					{
						int enemyNum = Integer.parseInt(nextCommand.substring(12, 13));
						if(currentRoom.enemies.get(enemyNum).currentHealth > 0)
						{
							int damage = rand.nextInt(21) - 10 + playerDamage * 2;
							currentRoom.enemies.get(enemyNum).currentHealth -= damage;
							System.out.println("Dealt "+damage+" to enemy "+enemyNum);
							if(currentRoom.enemies.get(enemyNum).currentHealth <= 0)
							{
								System.out.println("Killed enemy "+enemyNum);
								System.out.println("Gained "+currentRoom.enemies.get(enemyNum).soulReward+" souls");
								currentSouls += currentRoom.enemies.get(enemyNum).soulReward;
							}
							isHeavyAttacking = true;
						}
					}
				}
				else if(nextCommand.toLowerCase().equals("roll"))
				{
					Random rand = new Random();
					int chanceToDodge = rand.nextInt(101);
					if(chanceToDodge <= playerAGI)
					{
						isDodging = true;
					}
					else
					{
						System.out.println("You fail to roll out of the way");
					}
				}
				else if(nextCommand.toLowerCase().equals("block"))
				{
					Random rand = new Random();
					int chanceToBlock = rand.nextInt(100);
					if(chanceToBlock <= playerDefense)
					{
						isBlocking = true;
					}
					else
					{
						System.out.println("You fail to block any damage");
					}
				}
				else if(nextCommand.length() >= 6 && nextCommand.substring(0, 4).toLowerCase().equals("flee"))
				{
					int direction = Integer.parseInt(nextCommand.substring(5, 6));
					Flee(direction);
					isFleeing = true;
				}
				else
				{
					System.out.println("That is not a valid command");
					validCommand = false;
				}
				if(validCommand && isFleeing == false)
				{
					PrintEnemyStats(currentRoom);
					EnemyAI();
				}
			}
			//If there are no enemies in the room accept regular commands
			else
			{
				if(nextCommand.toLowerCase().equals("forward"))
				{
					if(currentRoom.nextRoom == null)
					{
						System.out.println("There is no room in that direction");
					}
				    else
					{
						if(currentRoom.requiredItem != null)
						{
							if(FindItem(currentRoom.requiredItem) != null)
							{
								MoveRooms(FindRoom(currentRoom.nextRoom));
							}
							else
							{
								System.out.println("You are missing the required item to move forwards");
							}
						}
						else
						{
							MoveRooms(FindRoom(currentRoom.nextRoom));
						}
						
					}
				}
				else if(nextCommand.toLowerCase().equals("left"))
				{
					if(currentRoom.leftRoom == null)
					{
						System.out.println("There is no room in that direction");
					}
					else
					{
						MoveRooms(FindRoom(currentRoom.leftRoom));
					}
				}
				else if(nextCommand.toLowerCase().equals("right"))
				{
					if(currentRoom.rightRoom == null)
					{
						System.out.println("There is no room in that direction");
					}
					else
					{
						MoveRooms(FindRoom(currentRoom.rightRoom));
					}
				}
				else if(nextCommand.toLowerCase().equals("back"))
				{
					if(currentRoom.previousRoom == null)
					{
						System.out.println("There is no room in that direction");
					}
					else
					{
						MoveRooms(FindRoom(currentRoom.previousRoom));
					}
				}
				else if(nextCommand.toLowerCase().equals("look"))
				{
					System.out.println(currentRoom.lookAround);
				}
				else if(nextCommand.toLowerCase().equals("search"))
				{
					if(currentRoom.items == null)
					{
						System.out.println("There is nothing to search in this room");
					}
					else
					{
						for(int x = 0; x < currentRoom.items.size(); x++)
						{
							ItemObject item = currentRoom.items.get(x);
							inventory.add((item));
							RemoveAddItemStats(false, item);
						}
					}
				}
				else if(nextCommand.toLowerCase().equals("inventory"))
				{
					OpenInventory();
				}
				else if(nextCommand.toLowerCase().equals("save"))
				{
					if(currentRoom.bonfire)
					{
						Save();
						System.out.println("Progress saved and health restored");
					}
					else
					{
						System.out.println("There is not a bonfire in the room you are currently in");
					}
				}
				else if(nextCommand.toLowerCase().equals("levelup"))
				{
					if(currentSouls >= soulsToLevelUp)
					{
						LevelUp();
					}
					else
					{
						System.out.println("You don't have enough souls to level up");
					}
				}
				else if(nextCommand.toLowerCase().equals("stats"))
				{
					System.out.println(playerDamage+" Damage");
					System.out.println(playerDefense+" Defense");
					System.out.println(playerAGI+" Agility");
					System.out.println(currentPlayerHealth+"/"+maxPlayerHealth+" Health");
					System.out.println(currentSouls+"/"+soulsToLevelUp+" Souls");
				}
				else if(nextCommand.toLowerCase().equals("instructions"))
				{
					Instructions();
				}
				else if(nextCommand.length() >= 6 && nextCommand.substring(0, 5).toLowerCase().equals("equip"))
				{
					int itemNum = Integer.parseInt((nextCommand.substring(6,7)));
					if(inventory.get(itemNum) != null && inventory.get(itemNum).slot != ItemObject.equipSlot.none)
					{
						EquipItem(inventory.get(itemNum));
					}
					else
					{
						System.out.println("This item is not equippable");
					}
				}
				else if(nextCommand.length() >= 4 && nextCommand.substring(0, 3).toLowerCase().equals("use"))
				{
					int itemNum = Integer.parseInt((nextCommand.substring(4,5)));
					if(inventory.get(itemNum) != null)
					{
						UseItem(inventory.get(itemNum));
					}
					else
					{
						System.out.println("No item in this slot");
					}
				}
				else if(nextCommand.length() >= 5 && nextCommand.substring(0, 4).toLowerCase().equals("drop"))
				{
					int itemNum = Integer.parseInt((nextCommand.substring(5,6)));
					if(inventory.get(itemNum) != null)
					{
						DropItem(inventory.get(itemNum));
					}
					else
					{
						System.out.println("No item in this slot");
					}
				}
				else if(nextCommand.toLowerCase().equals("stop"))
				{
					System.out.println("Ending game");
					gameRunning = false;
				}
				else
				{
					System.out.println("That is not a valid command");
				}
			}
			isBlocking = false;
			isDodging = false;
			isHeavyAttacking = false;
			Update();
			scan.close();			
		}	
	}
	public static void UseItem(ItemObject item)
	{
		if(item.healthGain > 0)
		{
			HealPlayer(false, item.healthGain);
		}
		else if(item.soulsGain > 0)
		{
			currentSouls += item.soulsGain;
		}
		else
		{
			System.out.println("This item cannot be used");
		}
	}
	public static void DropItem(ItemObject item)
	{
		RemoveAddItemStats(true, item);
		inventory.remove(inventory.indexOf(item));
		System.out.println("Dropped "+item.itemType);
	}
	public static void EquipItem(ItemObject item)
	{
		if(item.slot == ItemObject.equipSlot.weapon)
		{
			System.out.println("Equiped "+item.itemType);
			if(weaponSlot == null)
			{
				RemoveAddItemStats(false, item);
				weaponSlot = item;
			}
			else
			{
				RemoveAddItemStats(true, weaponSlot);
				RemoveAddItemStats(false, item);
				weaponSlot = item;
				System.out.println("Replaced "+weaponSlot.itemType+" with "+item.itemType);
			}
		}
		if(item.slot == ItemObject.equipSlot.offhand)
		{
			System.out.println("Equiped "+item.itemType);
			if(offhandSlot == null)
			{
				RemoveAddItemStats(false, item);
				offhandSlot = item;
			}
			else
			{
				RemoveAddItemStats(true, offhandSlot);
				RemoveAddItemStats(false, item);
				offhandSlot = item;
				System.out.println("Replaced "+offhandSlot.itemType+" with "+item.itemType);
			}
		}
		if(item.slot == ItemObject.equipSlot.helmet)
		{
			System.out.println("Equiped "+item.itemType);
			if(helmetSlot == null)
			{
				RemoveAddItemStats(false, item);
				helmetSlot = item;
			}
			else
			{
				RemoveAddItemStats(true, helmetSlot);
				RemoveAddItemStats(false, item);
				helmetSlot = item;
				System.out.println("Replaced "+helmetSlot.itemType+" with "+item.itemType);
			}
		}
		if(item.slot == ItemObject.equipSlot.chest)
		{
			System.out.println("Equiped "+item.itemType);
			if(chestSlot == null)
			{
				RemoveAddItemStats(false, item);
				chestSlot = item;
			}
			else
			{
				RemoveAddItemStats(true, chestSlot);
				RemoveAddItemStats(false, item);
				chestSlot = item;
				System.out.println("Replaced "+chestSlot.itemType+" with "+item.itemType);
			}
		}
		if(item.slot == ItemObject.equipSlot.leggings)
		{
			System.out.println("Equiped "+item.itemType);
			if(leggingsSlot == null)
			{
				RemoveAddItemStats(false, item);
				leggingsSlot = item;
			}
			else
			{
				RemoveAddItemStats(true, leggingsSlot);
				RemoveAddItemStats(false, item);
				leggingsSlot = item;
				System.out.println("Replaced "+leggingsSlot.itemType+" with "+item.itemType);
			}
		}
		if(item.slot == ItemObject.equipSlot.boots)
		{
			System.out.println("Equiped "+item.itemType);
			if(bootsSlot == null)
			{
				RemoveAddItemStats(false, item);
				bootsSlot = item;
			}
			else
			{
				RemoveAddItemStats(true, bootsSlot);
				RemoveAddItemStats(false, item);
				bootsSlot = item;
				System.out.println("Replaced "+bootsSlot.itemType+" with "+item.itemType);
			}
		}
	}
	public static void RemoveAddItemStats(boolean remove, ItemObject item)
	{
		if(remove)
		{
			if(item.healthStat > 0)
			{
				maxPlayerHealth -= item.healthStat;
				System.out.println("Removed "+item.healthStat+" max health");
			}
			else if(item.defenseStat > 0)
			{
				playerDefense -= item.defenseStat;
				System.out.println("Removed "+item.defenseStat+" defense");
			}
			else if(item.AGIstat > 0)
			{
				playerAGI -= item.AGIstat;
				System.out.println("Removed "+item.AGIstat+" agility");
			}
			else if(item.damageStat > 0)
			{
				playerDamage -= item.damageStat;
				System.out.println("Removed "+item.damageStat+" damage");
			}
		}
		else
		{
			if(item.healthStat > 0)
			{
				maxPlayerHealth += item.healthStat;
				System.out.println("Added "+item.healthStat+" max health");
			}
			else if(item.defenseStat > 0)
			{
				playerDefense += item.defenseStat;
				System.out.println("Added "+item.defenseStat+" defense");
			}
			else if(item.AGIstat > 0)
			{
				playerAGI += item.AGIstat;
				System.out.println("Added "+item.AGIstat+" agility");
			}
			else if(item.damageStat > 0)
			{
				playerDamage += item.damageStat;
				System.out.println("Added "+item.damageStat+" damage");
			}
		}
	}
	public static ItemObject FindItem(Item item)
	{
		for(int x = 0; x < inventory.size(); x++)
		{
			if(inventory.get(x).itemType == item)
			{
				return inventory.get(x);
			}
		}
		return null;
	}
	public static void Respawn()
	{
		System.out.println("You Died");
		MoveRooms(respawnRoom);
		currentPlayerHealth = maxPlayerHealth;
	}
	public static int DamagePlayer(int baseDamage, Enemy enemySource)
	{
		Random rand = new Random();
		int damage = 0;
		if(isHeavyAttacking)
		{
			baseDamage *= 2;
		}
		if(isBlocking)
		{
			double percentToBlock = playerDefense / 100.0;
			System.out.println(percentToBlock);
			damage = rand.nextInt(21) - 10 + baseDamage;
			System.out.println(damage);
			damage -= percentToBlock * damage;
			currentPlayerHealth -= damage;
			isBlocking = false;
			System.out.println("The "+enemySource.enemyType+" attacks you");
			System.out.println("You lose "+damage+" health");
		}
		else if(isDodging == false)
		{
			damage = rand.nextInt(21) - 10 + baseDamage;
			currentPlayerHealth -= damage;
			System.out.println("The "+enemySource.enemyType+" attacks you");
			System.out.println("You lose "+damage+" health");
		}
		else
		{
			System.out.println("You successfully dodge the attack");
			isDodging = false;
		}
		if(currentPlayerHealth <= 0)
		{
			Respawn();
		}
		return damage;
	}
	public static void HealPlayer(boolean toMax, int health)
	{
		if(toMax)
		{
			int healingDone = maxPlayerHealth - currentPlayerHealth;
			System.out.println("Healed for "+healingDone+" health");
			currentPlayerHealth = maxPlayerHealth;
		}
		else
		{
			System.out.println("Healed for "+health+" health");
			currentPlayerHealth += health;
			if(currentPlayerHealth > maxPlayerHealth)
			{
				currentPlayerHealth = maxPlayerHealth;
			}
		}
	}
	//First living enemy in the room takes an action
	public static void EnemyAI()
	{
		if(CheckForEnemies(currentRoom))
		{
			Enemy topEnemy = new Enemy();
			for(int x = 0; x < currentRoom.enemies.size(); x++)
			{
				if(currentRoom.enemies.get(x).currentHealth >= 0)
				{
					topEnemy = currentRoom.enemies.get(x);
					break;
				}
			}
			
			if(topEnemy.enemyType == Enemy.EnemyTypes.Warrior)
			{
				
			}
			else if(topEnemy.enemyType == Enemy.EnemyTypes.Archer)
			{
				DamagePlayer(topEnemy.damage, topEnemy);
			}
			else if(topEnemy.enemyType == Enemy.EnemyTypes.FireBomber)
			{
				
			}
			else if(topEnemy.enemyType == Enemy.EnemyTypes.Pikeman)
			{
				
			}
			else if(topEnemy.enemyType == Enemy.EnemyTypes.AsylumDemonBoss)
			{
				Random rand = new Random();
				int chance = rand.nextInt(101);
				if(topEnemy.preparingToAttack)
				{
					DamagePlayer(topEnemy.damage * 2, topEnemy);
					topEnemy.preparingToAttack = false;
				}
				
				else if(topEnemy.isDodging)
				{
					System.out.println("The demon does nothing");
					topEnemy.isDodging = false;
				}
				
				else if(topEnemy.isDodging == false && topEnemy.preparingToAttack == false)
				{
					if(chance <= 45)
					{
						DamagePlayer(topEnemy.damage, topEnemy);
					}
					else if(chance > 45 && chance <= 50)
					{
						System.out.println("The demon does nothing");
					}
					else if(chance > 50 && chance < 75)
					{
						System.out.println("The demon is preparing to attack");
						topEnemy.preparingToAttack = true;
					}
					else if(chance >= 75 && chance <= 100)
					{
						System.out.println("The demon leaps back");
						topEnemy.isDodging = true;
					}
				}
			}
			else if(topEnemy.enemyType == Enemy.EnemyTypes.Hollow)
			{
				System.out.println("The hollow does nothing");
			}
			else if(topEnemy.enemyType == Enemy.EnemyTypes.RollingBoulder)
			{
				if(isDodging == false)
				{
					System.out.println("The boulder crashes into you");
					DamagePlayer(10000, topEnemy);
				}
				else
				{
					topEnemy.currentHealth = 0;
					System.out.println("You roll out of the way of the boulder and it crashes into the wall behind you");
				}
			}
		}
	}
	public static void Flee(int direction)
	{
		if(currentRoom.nextRoom != null && direction == 0)
		{
			MoveRooms(FindRoom(currentRoom.nextRoom));
		}
		else if(currentRoom.previousRoom != null && direction == 1)
		{
			MoveRooms(FindRoom(currentRoom.previousRoom));
		}
		else if(currentRoom.leftRoom != null && direction == 2)
		{
			MoveRooms(FindRoom(currentRoom.leftRoom));
		}
		else if(currentRoom.rightRoom != null && direction == 3)
		{
			MoveRooms(FindRoom(currentRoom.rightRoom));
		}
		else
		{
			System.out.println("No room in that direction");
		}
	}
	public static void Instructions()
	{
		System.out.println("The following commands will control what you do");
		System.out.println("---------------General---------------");
		System.out.println("These commands can only be used out of combat");
		System.out.println("\"Instructions\": Show this list of instructions again");
		System.out.println("\"Forward\": Attempt to move to the room in front of you");
		System.out.println("\"Left\": Attempt to move to the room to the left of you");
		System.out.println("\"Right\": Attempt to move to the room to the right of you");
		System.out.println("\"Back\": Attempt to move to the room behind you");
		System.out.println("\"Look\": Look around the room you are currently in");
		System.out.println("\"Search\": Search the room you are currently in");
		System.out.println("\"Save\": Save the game if you are in a room with a bonfire");
		System.out.println("\"Inventory\": Look in inventory");
		System.out.println("\"Stats\": Shows all player stats");
		System.out.println("\"LevelUp\": Level up if you have enough souls");
		System.out.println("\"Stop\": Quit Game (Will permanently lose progress");
		System.out.println("---------------Inventory---------------");
		System.out.println("\"Use (item number)\": Use an item from your inventory");
		System.out.println("\"Drop (item number)\": Drop an item from you inventory");
		System.out.println("\"Equip (item number)\": Equip an item from you inventory");
		System.out.println("\"Inspect (item number)\": Inspect an item from you inventory to view its stats");
		System.out.println("---------------Combat---------------");
		System.out.println("\"Attack (enemy number)\": Deal damage to an enemy");
		System.out.println("\"Attack (enemy number)\": Deal heavy damage to an enemy, but take extra damage from enemy attacks");
		System.out.println("\"Roll\": Attempt to roll out of the way of the next attack from an enemy");
		System.out.println("\"Block\": Attempt to block next attack from an enemy");
		System.out.println("\"Flee (direction)\": Run from current enemies (0 for forward, 1 for backward, 2 for left, 3 for right)");
	}
	public static void OpenInventory()
	{
		if(inventory.size() > 0)
		{
			System.out.println("Inventory:");
			for(int x = 0; x < inventory.size(); x++)
			{
				System.out.println(x+": "+inventory.get(x).itemType);
			}
		}
		else
		{
			System.out.println("Inventory is empty");
		}
	}
	public static void Save()
	{
		respawnRoom = currentRoom;
		HealPlayer(true, 0);
		for(int x = 0; x < roomList.size(); x++)
		{
			if(roomList.get(x).enemies != null)
			{
				for(int y = 0; y < roomList.get(x).enemies.size(); y++)
				{
					roomList.get(x).enemies.get(y).currentHealth = roomList.get(x).enemies.get(y).maxHealth;			
				}
			}
		}
	}
	public static void LevelUp()
	{
		System.out.println("Leveled Up");
		System.out.println("Pick a stat to upgrade");
		System.out.println("0: Base Damage");
		System.out.println("1: Base Defense");
		System.out.println("2: Base Agility");
		System.out.println("3: Base Health");
		Scanner scan = new Scanner(System.in);
		int statToLevelUp = scan.nextInt();
		if(statToLevelUp == 0)
		{
			System.out.println("Increased damage by 5");
			playerDamage += 5;
		}
		else if(statToLevelUp == 1)
		{
			System.out.println("Increased defense by 4");
			playerDefense += 3;
		}
		else if(statToLevelUp == 2)
		{
			System.out.println("Increased agility by 3");
			playerAGI += 3;
		}
		else if(statToLevelUp == 3)
		{
			System.out.println("Increased health by 25");
			maxPlayerHealth += 25;
		}
		HealPlayer(true, 0);
		currentSouls = currentSouls - soulsToLevelUp;
		soulsToLevelUp *= 1.15;
		scan.close();
	}
	public static boolean CheckForEnemies(RoomObject room)
	{
		boolean livingEnemiesInRoom = false;
		if(room.enemies != null && room.enemies.size() > 0)
		{
			for(int x = 0; x < room.enemies.size(); x++)
			{
				if(room.enemies.get(x).currentHealth > 0)
				{
					livingEnemiesInRoom = true;
				}
			}
		}
		return livingEnemiesInRoom;
	}
	public static void PrintEnemyStats(RoomObject room)
	{
		if(CheckForEnemies(currentRoom))
		{
			for(int x = 0; x < room.enemies.size(); x++)
			{
				if(room.enemies.get(x).currentHealth > 0)
				{
					System.out.println("Enemy "+x+": "+room.enemies.get(x).enemyType+" "+room.enemies.get(x).currentHealth+"/"+room.enemies.get(x).maxHealth+" health, "+room.enemies.get(x).damage+" damage, "+room.enemies.get(x).defense+" defense");
				}
			}
		}
	}
}
