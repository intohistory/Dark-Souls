
public class Enemy 
{
	enum EnemyTypes{Warrior, Archer, FireBomber, Pikeman, AsylumDemonBoss, Hollow, RollingBoulder};
	
	public int currentHealth;
	public int maxHealth;
	
	public int damage;
	public int defense;
	
	public int soulReward;
	
	public EnemyTypes enemyType;
	
	public boolean preparingToAttack = false;
	public boolean isDodging = false;
	
	public static Enemy CreateEnemy(EnemyTypes type)
	{
		Enemy currentEnemy = new Enemy();
		currentEnemy.enemyType = type;
		if(type == EnemyTypes.Warrior)
		{
			currentEnemy.maxHealth = 350;
			currentEnemy.currentHealth = 350;
			currentEnemy.damage = 100;
			currentEnemy.defense = 20;
			currentEnemy.soulReward = 50;
		}
		else if(type == EnemyTypes.Archer)
		{
			currentEnemy.maxHealth = 250;
			currentEnemy.currentHealth = 250;
			currentEnemy.damage = 130;
			currentEnemy.defense = 10;
			currentEnemy.soulReward = 45;
		}
		else if(type == EnemyTypes.FireBomber)
		{
			currentEnemy.maxHealth = 275;
			currentEnemy.currentHealth = 275;
			currentEnemy.damage = 150;
			currentEnemy.defense = 10;
			currentEnemy.soulReward = 55;
		}
		else if(type == EnemyTypes.Pikeman)
		{
			currentEnemy.maxHealth = 450;
			currentEnemy.currentHealth = 450;
			currentEnemy.damage = 100;
			currentEnemy.defense = 85;
			currentEnemy.soulReward = 75;
		}
		else if(type == EnemyTypes.AsylumDemonBoss)
		{
			currentEnemy.maxHealth = 1000;
			currentEnemy.currentHealth = 1000;
			currentEnemy.damage = 175;
			currentEnemy.defense = 100;
			currentEnemy.soulReward = 400;
		}
		else if(type == EnemyTypes.Hollow)
		{
			currentEnemy.maxHealth = 75;
			currentEnemy.currentHealth = 75;
			currentEnemy.damage = 0;
			currentEnemy.defense = 0;
			currentEnemy.soulReward = 10;
		}
		else if(type == EnemyTypes.RollingBoulder)
		{
			currentEnemy.maxHealth = 1000;
			currentEnemy.currentHealth = 1000;
			currentEnemy.damage = 1000;
			currentEnemy.defense = 0;
			currentEnemy.soulReward = 0;
		}
		return currentEnemy;
	}
}
