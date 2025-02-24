 

import javafx.scene.paint.Color; 
import java.util.List;
import java.util.stream.Stream;

/**
 * Simplest form of life.
 * Fun Fact: Mycoplasma are one of the simplest forms of life.  A type of
 * bacteria, they only have 500-1000 genes! For comparison, fruit flies have
 * about 14,000 genes.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2022.01.06
 */

public class Mycoplasma extends Cell {
    
    /**
     * Create a new Mycoplasma.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Mycoplasma(Field field, Location location, Color col) {
        super(field, location, col);
    }

    /**
    * This is how the Mycoplasma decides if it's alive or not
    */
    public void act() {
        neighbours = getField().getLivingNeighbours(getLocation(), Mycoplasma.class); // updates neighbour list
        updateInfectionState();
    
        if (isAlive() && !isInfected()) {
            if (neighbours.size() == 2 || neighbours.size() == 3) {
                setNextState(true);   
            }
            
            else if (neighbours.size() < 2 || neighbours.size() > 3) {
                setNextState(false);
            }
        }
        else if(!isAlive() && !isInfected()) {
            setNextState(false);
        }
    }
    
    /**
     * This is how the Mycoplasma checks if it can be revived
     * If the Mycoplasma cell has 3 adjacent cells, it can be revived
     * @return True if the cell can be revived
     */
    public boolean canRevive(){
        neighbours = getField().getLivingNeighbours(getLocation(), Mycoplasma.class); // updates neighbour list
        
        if(neighbours == null) {
            return false;
        }
        else {
            return neighbours.size() == 3;
        }
    }
}
