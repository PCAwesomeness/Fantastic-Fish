package fantastic.entities;

import java.util.Random;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import fantastic.items.FantasticItems;

public class EntityLobster extends EntityWaterMob
{

	private static Random rand = new Random();
	private boolean hasNotSpawned = true;

	public EntityLobster(World par1)
	{
		this(par1, rand.nextInt(3));
	}

	public EntityLobster(World par1World, int par2)
	{
		super(par1World);
		this.setTexture(par2);
        this.getNavigator().setAvoidsWater(false);
	this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPlayer.class, 5, 0.4F, 0.5F));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.4D));
		this.tasks.addTask(6, new EntityAIWander(this, 0.4D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.ignoreFrustumCheck = true;
		this.setSize(1.0F, 1.0F);
	}


	

	public boolean getHasNotSpawned()
	{
		return this.hasNotSpawned;
	}
	
	public void setHasNotSpawned(boolean par1)
	{
		this.hasNotSpawned = par1;
	}
	
	protected boolean isAIEnabled()
	{
		return true;

	}

	public int getTotalArmorValue()
	{
		return 2;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
		
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(5);

	}

	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume()
	{
		return 0.6F;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{

		if (rand.nextInt(100) >= 25)
		{
			return "fantastic:lobster_hurt";
		} else
		{
			return null;
		}

	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		if(rand.nextInt(10) < 5)
		return "fantastic:lobster_idle1";
		else
			return "fantastic:lobster_idle2";

	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return null;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		super.dropFewItems(par1, par2);
		this.entityDropItem(new ItemStack(FantasticItems.tailRaw, 1), 0.0F);
		if(rand.nextInt(3) == 0)
		{
			this.entityDropItem(new ItemStack(FantasticItems.clawRaw, 1 + rand.nextInt(2)), 0.0F);
		}

	}

	

	

	protected void fall(float var1)
	{
	}


	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource par1DamageSource)
	{
		super.onDeath(par1DamageSource);

	}
	public void setTexture(int par1)
	{
		this.dataWatcher.updateObject(19, Integer.valueOf(par1));
	}
	
	

	public int getTexture()
	{
		return this.dataWatcher.getWatchableObjectInt(19);
	}
	
	

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(19, Integer.valueOf(0));
		this.dataWatcher.addObject(21, Float.valueOf(0));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Texture", this.getTexture());
		par1NBTTagCompound.setBoolean("HasNotSpawned", this.getHasNotSpawned());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setTexture(par1NBTTagCompound.getInteger("Texture"));
		this.setHasNotSpawned(par1NBTTagCompound.getBoolean("HasNotSpawned"));
	}

	
	/**
	 * randomly selected ChunkCoordinates in a 7x6x7 box around the bat (y
	 * offset -2 to 4) towards which it will fly. upon getting close a new
	 * target will be selected
	 */
	public boolean outOfWater;
	
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		if(this.isInWater())
		{
				setIsOutOfWater(false);
		}
			
		else
		{
				setIsOutOfWater(true);
		}
		
		
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
		
	}


	
	public boolean getIsOutOfWater()
	{
		return outOfWater;
	}
	
	public void setIsOutOfWater(boolean par1)
	{
		outOfWater = par1;
	}
	
	
	
	 public boolean interact(EntityPlayer par1EntityPlayer)
	    {
		 	ItemStack item = par1EntityPlayer.inventory.getCurrentItem();
		 	if(item != null && item.getItem() == FantasticItems.fishingNet)
		 	{
		 		ItemStack lobsterDrop = new ItemStack(FantasticItems.lobster, 1);
		 		int damage = 100;
		 		
		 		lobsterDrop.setItemDamage(this.getTexture());
		 		if(this.hasCustomNameTag())
		 		{
			 		lobsterDrop.setStackDisplayName(this.getCustomNameTag());
		 		}
		 		EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, lobsterDrop);
		 		if(!this.worldObj.isRemote)
		 		{
		 			this.worldObj.spawnEntityInWorld(entityitem);
		 		}
                if(!par1EntityPlayer.capabilities.isCreativeMode)
                {
                	item.damageItem(damage, par1EntityPlayer);
                }
                this.setDead();
		 		
		 	}
		 	else if(item != null && item.getItem() == Items.name_tag)
		 	{
		 		if(item.hasDisplayName())
		 		{
		 			this.setCustomNameTag(item.getDisplayName());
		 			item.stackSize--;
		 		}
		 	}
		 	
			return true;
	    }
	 /**
	     * Determines if an entity can be despawned, used on idle far away entities
	     */
	    protected boolean canDespawn()
	    {
	        return this.hasCustomNameTag() ? false : true;
	    }
}
