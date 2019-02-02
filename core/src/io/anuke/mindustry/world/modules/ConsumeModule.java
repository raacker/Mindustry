package io.anuke.mindustry.world.modules;

import io.anuke.mindustry.entities.type.TileEntity;
import io.anuke.mindustry.world.consumers.Consume;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ConsumeModule extends BlockModule{
    private boolean valid;
    private TileEntity entity;

    public void update(TileEntity entity){
        this.entity = entity;
        boolean prevValid = valid();
        valid = true;

        for(Consume cons : entity.tile.block().consumes.all()){
            if(cons.isUpdate() && prevValid && entity.tile.block().shouldConsume(entity.tile) && cons.valid(entity.getTile().block(), entity)){
                cons.update(entity.getTile().block(), entity);
            }

            if(!cons.isOptional()){
                valid &= cons.valid(entity.getTile().block(), entity);
            }
        }
    }

    public boolean valid(){
        return valid && (entity == null || entity.tile.block().canProduce(entity.tile));
    }

    @Override
    public void write(DataOutput stream) throws IOException{
        stream.writeBoolean(valid);
    }

    @Override
    public void read(DataInput stream) throws IOException{
        valid = stream.readBoolean();
    }
}
