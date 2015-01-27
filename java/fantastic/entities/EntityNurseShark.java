package fantastic.entities;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import fantastic.items.FantasticItems;

public class EntityNurseShark extends EntityWaterMob
{

	private Vec3 currentSwimTarget;
	private Vec3 bobberTarget;
	private int ignoreTimer = (int)(500 *this.getRenderSize());
	private int jumpTimer = 60;
	public int extraSpeed = 1;
	boolean isBobber;
	public boolean outOfWater;
	private static Random rand = new Random();
	

	private boolean hasNotSpawned = true;

	public EntityNurseShark(World par1)
	{
		this(par1, rand.nextInt(3), rand.nextInt(50));
	}

	public EntityNurseShark(World par1World, int par3, int par4)
	{
		super(par1World);
		this.setPreRenderSize(1);
		this.setFish(par3);
		this.setPreTexture(par4);
        this.getNavigator().setAvoidsWater(false);
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.4D));
		this.tasks.addTask(6, new EntityAIWander(this, 0.4D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 2.0F, false));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
		this.ignoreFrustumCheck = true;
		this.setSize(1.2F, 1.1F);
	}


	private void setPreTexture(int par4) 
	{
		if(par4 > 40)
		{
			this.setTexture(2);
		}
		else if(par4 >= 35)
		{
			this.setTexture(1);
		}
		else
		{
			this.setTexture(0);
		}
		
	}

	private void setPreRenderSize(int par3) 
	{
		this.setRenderSize(1.0F);
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
		return 0;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D );
		
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(10);

	}

	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEFINED;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{

		return null;
		

	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
	
			return null;
		
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
		if(this.getFish() > 0)
		this.entityDropItem(new ItemStack(Items.fish, this.getFish()), 0.0F);
		
		if(rand.nextInt(10) == 0)
			this.entityDropItem(new ItemStack(FantasticItems.sharkFin, 1), 0.0F);

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
	//This returns the fishing hook
		//getBobber().bobber will retrieve the fish that is attached.
		//It should equal this, otherwise, remove this reference 
		//getBobber().field_146042_b returns the player that is fishing
		//it gets assigned locally to 'playerThatsFishing'
		public EntityFishHook getBobber()
		{
			if(worldObj.getEntityByID(getBobberID()) instanceof EntityFishHook){
				EntityFishHook efh = (EntityFishHook) worldObj.getEntityByID(getBobberID());
				efh.field_146043_c = this;
				return efh;
			}
			return null;
		}
		public int getBobberID()
		{
			return this.dataWatcher.getWatchableObjectInt(22);
		}
	public int getIsOutOfWater()
	{
		return this.dataWatcher.getWatchableObjectInt(17);
	}
	
	public void setIsOutOfWater(int par1)
	{
		this.dataWatcher.updateObject(17, Integer.valueOf(par1));
	}

	
	public void setRenderSize(float par1) 
	{
		this.dataWatcher.updateObject(21, Float.valueOf(par1));
		
	}
	public void setFish(int par1) 
	{
		this.dataWatcher.updateObject(25, Integer.valueOf(par1));
		
	}
	public void setTexture(int par1) 
	{
		this.dataWatcher.updateObject(19, Integer.valueOf(par1));
		
	}
	public void setBobberID(int par1)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()){
			if(worldObj.getEntityByID(getBobberID()) instanceof EntityFishHook){
				EntityFishHook efh = (EntityFishHook) worldObj.getEntityByID(getBobberID());
				if(efh.field_146043_c != this){
					((EntityBasicFish)efh.field_146043_c).setBobberID(-1);
					efh.field_146043_c = this;
				}
			}
			this.dataWatcher.updateObject(22, Integer.valueOf(par1));
		}
		
	}
	public int getTexture()
	{
		return this.dataWatcher.getWatchableObjectInt(19);
	}
	
	public float getRenderSize()
	{
		return this.dataWatcher.getWatchableObjectFloat(21);
	}
	public int getFish()
	{
		return this.dataWatcher.getWatchableObjectInt(25);
	}
	

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(17, Integer.valueOf(0));
		this.dataWatcher.addObject(19, Integer.valueOf(0));
		this.dataWatcher.addObject(21, Float.valueOf(0));
		this.dataWatcher.addObject(22, Integer.valueOf(-1));
		this.dataWatcher.addObject(25, Integer.valueOf(0));
		
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setFloat("RenderSize", this.getRenderSize());
		par1NBTTagCompound.setBoolean("HasNotSpawned", this.getHasNotSpawned());
		par1NBTTagCompound.setInteger("isOutOfWater", this.getIsOutOfWater());
		par1NBTTagCompound.setInteger("Fish", this.getFish());
		par1NBTTagCompound.setInteger("Texture", this.getTexture());
		
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setRenderSize(par1NBTTagCompound.getFloat("RenderSize"));
		this.setHasNotSpawned(par1NBTTagCompound.getBoolean("HasNotSpawned"));
		this.setIsOutOfWater(par1NBTTagCompound.getInteger("isOutOfWater"));
		this.setFish((par1NBTTagCompound.getInteger("Fish")));
		this.setTexture(par1NBTTagCompound.getInteger("Texture"));
		
	}

	
	/**
	 * randomly selected ChunkCoordinates in a 7x6x7 box around the bat (y
	 * offset -2 to 4) towards which it will fly. upon getting close a new
	 * target will be selected
	 */
	
	public void setPosition(double par1, double par3, double par5) {
		super.setPosition(par1, par3, par5);
		if(currentSwimTarget == null){
			currentSwimTarget = Vec3.createVectorHelper(par1, par3, par5);
		}
	}
	EntityPlayer playerThatsFishing = null;
	int attackTimer = 40;
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		if(getBobber() != null && getBobber().field_146042_b != null && (getBobber().field_146042_b.fishEntity == null)){
			setBobberID(-1);
		}

		
		if(this.isInWater())
		{
			--attackTimer;
			
			
			setIsOutOfWater(1);
			if(getBobber() == null || getBobber().field_146042_b == null || getBobber().isDead || getBobber().field_146042_b.isDead)
			{
				setBobberID(-1);
				this.bobberTarget = null;
			}
			if(ignoreTimer <= 0 && playerThatsFishing != null && playerThatsFishing.fishEntity != null)
			{
				if(((EntityFishHook)playerThatsFishing.fishEntity).field_146043_c == null)
				{
					((EntityFishHook)playerThatsFishing.fishEntity).field_146043_c = this;
					setBobberID(playerThatsFishing.fishEntity.getEntityId());
					((EntityFishHook)playerThatsFishing.fishEntity).field_146042_b.fishEntity = getBobber();
				}
			}
			playerThatsFishing = null;
			EntityPlayer avoidPlayer= null;
			
			if(getBobber() != null)
			{
				playerThatsFishing = getBobber().field_146042_b;
			}
			
			if(playerThatsFishing != null && playerThatsFishing.fishEntity == null)
			{
				playerThatsFishing = null;
				setBobberID(-1);
				this.bobberTarget = null;
			}
			
			if(playerThatsFishing != null)
			{
				setBobberID(playerThatsFishing.fishEntity.getEntityId());
				this.ignoreTimer = 0;
				this.bobberTarget = null;
			}
			if(ignoreTimer <=0)
			{
				ignoreTimer = (int) (200*this.getRenderSize());
				bobberTarget = null;
			}
			//Now this will search for all three in first pass.
			if(playerThatsFishing == null)
			{
				if(getBobber() == null)
				{
					lookForBobber(this.posX, this.posY, this.posZ);
				}
				if(playerThatsFishing == null && getBobber() != null)
				{
					playerThatsFishing = getBobber().field_146042_b;
				}
				if(worldObj.getClosestPlayerToEntity(this, 20) != null && this.getFish() == -1 && attackTimer < 0)
				{
					avoidPlayer = worldObj.getClosestPlayerToEntity(this, 20);
					
					if(this.getDistanceToEntity(avoidPlayer) < 1.0)
					{
						avoidPlayer.attackEntityFrom(DamageSource.generic, 4.0F);
						attackTimer = 60;
					}
					if(avoidPlayer.isInWater())
					currentSwimTarget = Vec3.createVectorHelper(avoidPlayer.posX, avoidPlayer.posY, avoidPlayer.posZ);
				}
			}
			
			if(bobberTarget != null)
			{
				this.currentSwimTarget = bobberTarget;
				ignoreTimer--;
			}
			
			
			
			if(this.currentSwimTarget != null)
			{
				approachTarget(this.posX, this.posY, this.posZ, 1);
			}
			
			if(!this.worldObj.isAirBlock((int)this.posX, (int)this.posY+1, (int)this.posZ) && !this.worldObj.isAirBlock((int)this.posX, (int)this.posY+2, (int)this.posZ))
			{
				this.motionY += 0.0160829F;
			}
			findRandomTarget(this.posX, this.posY, this.posZ, false);
		}
		else
		{
			setIsOutOfWater(0);
			this.setJumping(true);
			if(this.getFish() >= 0)
			this.dropItem(Items.fish, this.getFish());
			this.setFish(0);
			if(this.onGround)
				this.addVelocity(0.1*rand.nextDouble() - 0.1*rand.nextDouble(), 0F, 0.1*rand.nextDouble() - 0.1*rand.nextDouble());
		}
		
	}
	//Math converted to use more accurate and readable Vec3's
	private void approachTarget(double X, double Y, double Z, int speed) 
	{
		Vec3 vec3 = Vec3.createVectorHelper(this.currentSwimTarget.xCoord + 0.5D - X, this.currentSwimTarget.yCoord + 0.1D - Y, this.currentSwimTarget.zCoord + 0.5D - Z).normalize();
			this.motionX += (vec3.xCoord * 0.5D - this.motionX) * 0.05000000149011612D * speed;
			this.motionY += (vec3.yCoord * 0.699999988079071D - this.motionY) * 0.05000000149011612D * speed;
			this.motionZ += (vec3.zCoord * 0.5D - this.motionZ) * 0.05000000149011612D * speed;
		
			float f = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
			float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
			this.moveForward = 0.1F * speed;
			this.rotationYaw += f1;
			this.motionY += 0.0130829F;
			if(getBobber() != null)
			{
				getBobber().setPosition(this.posX, this.posY, this.posZ);
			}
			Block blockID = worldObj.getBlock(MathHelper.floor_double(this.currentSwimTarget.xCoord), MathHelper.floor_double(this.currentSwimTarget.yCoord), MathHelper.floor_double(this.currentSwimTarget.zCoord));
		if(currentSwimTarget != null && (currentSwimTarget.squareDistanceTo((int)X, (int)Y, (int)Z) < 3 || blockID == null || !(blockID instanceof BlockLiquid)))
		{
			currentSwimTarget = null;
		}
	}

	private void setEscape(EntityPlayer theAngler, double X, double Y, double Z, boolean Jump) 
	{
		//The following line will take the distance difference of the two entity locations and normalize them.
		//This reduces the distances to their fractional ratios, ie, > -1.0 and < 1.0
		Vec3 escapeDirection = Vec3.createVectorHelper(X - theAngler.posX, 0, Z - theAngler.posZ).normalize();
		//Not much to say here, it assigns the pathing distance check to be performed 3^2 blocks away in the direction
		//the player is facing.
		xMovement = (int) Math.round(escapeDirection.xCoord * 3);
		yMovement = (int) Math.round(escapeDirection.yCoord * 3);
		zMovement = (int) Math.round(escapeDirection.zCoord * 3);
        currentSwimTarget = Vec3.createVectorHelper(X, Y, Z);
        //The above will enforce a direction opposing the player
        //if any water is in that direction, otherwise it will change
        //direction until it finds a path opposing the player
        
        //This line will enforce a pathing check to be performed so the fish can change direction if needed
        movementskip = 0;
        //Skip the callback to this method since our directional data is set now
        findRandomTarget(X, Y, Z, true);
        //double speed when running
		this.extraSpeed = 2;
		
		if(!this.worldObj.isAirBlock((int)X, (int)Y+1, (int)Z))
		{
			this.motionY += 0.0360829F;
		}
		
		else if(Jump)
		{
			this.jumpTimer--;
			if(this.jumpTimer < 0)
			{
				this.motionY += 0.74F;
				this.jumpTimer = 60;
			}	
		}
		
	}
	
	int movementskip = 0;
	int xMovement = -1,yMovement = -1,zMovement = -1;
	//If force is set to true, no check will be done to see if a fish is attached.
	//False will send to setEscape is necessary, which then recalls this method with force
	//set to true.
	private void findRandomTarget(double X, double Y, double Z, boolean force) 
	{
		int slowTick = this.ticksExisted/20;
		xMovement = xMovement + (5 * (int)Math.sin(slowTick));
		zMovement = zMovement + (5 * (int)Math.cos(slowTick));
		yMovement = yMovement - 5;
		if(isInWater()){
			movementskip--;
			if(!force && getBobber() != null && getBobber().field_146043_c == this && getBobber().field_146042_b != null){
				setEscape(getBobber().field_146042_b, this.posX, this.posY, this.posZ, true);
				return;
			}
			if(movementskip <= 0){
				//Will cause server and client to produce random yet identical rolls.
				rand.setSeed(getEntityId() + chunkCoordX + chunkCoordY + chunkCoordZ);
				movementskip = rand.nextInt(15);
				int randomWaterCheck = 0;
				
				Block blockID1 = Blocks.bedrock;
				Block blockID2 = Blocks.bedrock;
				Vec3 newPos = null;
				//Give up after 20 rolls
				//Bad luck or cannot move in the current conditions
				
				while(randomWaterCheck < 20){
					//reuse last movement unless a wall is there.
					if(randomWaterCheck == 0 && xMovement != -1){
						newPos = Vec3.createVectorHelper(X + xMovement, Y + yMovement, Z + zMovement);
						blockID1 = worldObj.getBlock(MathHelper.floor_double(newPos.xCoord - (this.width / 2.0f)), MathHelper.floor_double(newPos.yCoord), MathHelper.floor_double(newPos.zCoord - (this.width / 2.0f)));
						blockID2 = worldObj.getBlock(MathHelper.floor_double(newPos.xCoord + (this.width / 2.0f)), MathHelper.floor_double(newPos.yCoord), MathHelper.floor_double(newPos.zCoord + (this.width / 2.0f)));
						if(blockID1 instanceof BlockLiquid){
							if(blockID2 instanceof BlockLiquid) 
								break;
						}
						blockID1 = Blocks.bedrock;
						blockID2 = Blocks.bedrock;
					}
					randomWaterCheck++;
					xMovement = (rand.nextInt(10) - 5);
					yMovement = (rand.nextInt(2) - 1);
					zMovement = (rand.nextInt(10) - 5);
					newPos = Vec3.createVectorHelper(X + xMovement, Y + yMovement, Z + zMovement);
					blockID1 = worldObj.getBlock(MathHelper.floor_double(newPos.xCoord - (this.width / 2.0f)), MathHelper.floor_double(newPos.yCoord), MathHelper.floor_double(newPos.zCoord - (this.width / 2.0f)));
					blockID2 = worldObj.getBlock(MathHelper.floor_double(newPos.xCoord + (this.width / 2.0f)), MathHelper.floor_double(newPos.yCoord), MathHelper.floor_double(newPos.zCoord + (this.width / 2.0f)));
					if(blockID1 instanceof BlockLiquid){
						if(blockID2 instanceof BlockLiquid) break;
					}
					blockID1 = Blocks.bedrock;
					blockID2 = Blocks.bedrock;
				}
				if(blockID1 != Blocks.bedrock){
					this.currentSwimTarget = newPos;
					this.extraSpeed = 1;
				}
			}
		}
	}	float counter = 0;

	private void lookForBobber(double X, double Y, double Z) 
	{
		//List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(8.0D, 8.0D, 8.0D));
		Vec3 normalizer = Vec3.createVectorHelper(this.motionX, this.motionY, this.motionZ).normalize();
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.copy().expand(Math.abs(normalizer.xCoord * 3.0D) + 1, Math.abs(normalizer.yCoord * 3.0D) + 1, Math.abs(normalizer.zCoord * 3.0D) + 1));
		for (int l = 0; l < list.size(); ++l)
		{
			Entity entity1 = (Entity) list.get(l);
			
			if(entity1 instanceof EntityFishHook)
			{
				if(ignoreTimer<=0)
				{
					if(((EntityFishHook)entity1).field_146043_c == null)
					{
						((EntityFishHook)entity1).field_146043_c = this;
						setBobberID(entity1.getEntityId());
						((EntityFishHook)entity1).field_146042_b.fishEntity = getBobber();
					}
				}
				else
				{
					bobberTarget = Vec3.createVectorHelper(entity1.posX + 3*Math.sin(counter+=0.05), entity1.posY-3, entity1.posZ + 3*Math.sin(counter+=0.05));
					playerThatsFishing = ((EntityFishHook)entity1).field_146042_b;
				}
					
				
				
			}
		}
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
		
	}

	protected void collideWithEntity(Entity par1Entity)
	{
		if(par1Entity instanceof EntityFishHook)
		{
			EntityFishHook efh = (EntityFishHook) par1Entity;
			if(efh.field_146043_c == null){
				setBobberID(efh.getEntityId());
				getBobber().field_146043_c = this;
				efh.field_146042_b.fishEntity = getBobber();
				setEscape(efh.field_146042_b, this.posX, this.posY, this.posZ, true);
			}
		}
		super.collideWithEntity(par1Entity);
	}
	
	
	
	
	
	
	 public boolean interact(EntityPlayer par1EntityPlayer)
	    {
		 	ItemStack item = par1EntityPlayer.inventory.getCurrentItem();
		 	if(item != null && item.getItem() == FantasticItems.fishingNet)
		 	{
		 		par1EntityPlayer.swingItem();
		 		int damage = 10;

		 		

		 			if(this.getFish() > 0)
		 			{
		 				if(!this.worldObj.isRemote)
		 				this.dropItem(Items.fish, this.getFish());
		 				this.setFish(-1);
		 				
		 			}
		 			else
		 			{
		 			
		 		
		 		
		 			ItemStack droppedFish = new ItemStack(FantasticItems.randomFish, 1);
			 		droppedFish.setItemDamage(1 + this.getTexture());
			 		if(this.hasCustomNameTag())
			 		{
				 		droppedFish.setStackDisplayName(this.getCustomNameTag());
			 		}
			 		EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, droppedFish);
		 			if(!this.worldObj.isRemote)
		 			{
		 				this.worldObj.spawnEntityInWorld(entityitem);
		                this.setDead();
		 			}
		 			}

		 			
		 		
		 		if(!par1EntityPlayer.capabilities.isCreativeMode)
	 			{
	 				item.damageItem(damage, par1EntityPlayer);
	 			}
		 		
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