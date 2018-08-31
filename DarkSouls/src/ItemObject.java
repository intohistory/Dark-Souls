
public class ItemObject 
{
	public enum equipSlot {weapon, offhand, helmet, chest, leggings, boots, ring, none};
	public mainGame.Item itemType;
	public int healthStat;
	public int defenseStat;
	public int AGIstat;
	public int damageStat;
	public int soulsGain;
	public int healthGain;
	public equipSlot slot;
	
	public static ItemObject CreateItem(int healthStatToAdd, int defenseStatToAdd, int AGIStatToAdd, int damageStatToAdd, int soulsGainToAdd, int healthGainToAdd, mainGame.Item itemTypeToAdd, equipSlot slotToAdd)
	{
		ItemObject item = new ItemObject();
		item.healthStat = healthStatToAdd;
		item.defenseStat = defenseStatToAdd;
		item.AGIstat = AGIStatToAdd;
		item.damageStat = damageStatToAdd;
		item.soulsGain = soulsGainToAdd;
		item.healthGain = healthGainToAdd;
		item.itemType = itemTypeToAdd;
		item.slot = slotToAdd;
		return item;
	}
}
