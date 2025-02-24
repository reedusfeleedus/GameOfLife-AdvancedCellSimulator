 

import javafx.scene.paint.Color; 
import java.util.List;

/**
 * Chromacystis is a bioluminescent micro-organism that glows different colors based on its mood.
 * This cell has a parasitic symbiotic relation with Phasophyta. When near a phasophyta for too long, it dies (Suffers from relation) 
 * =================================================================================================================================
 * Rules
 * Lonely(Blue): 1 neighbour
 * Happy(Green) : 2 neighbours
 * Stressed(Red) : 3 neighbours
 * Cell dies when there's more than 3 neighbours.
 * Dead cells come to life glowing yellow when there's exactly 2 neighbours.
 */

public class Chromacystis extends Cell {
    
    public static final Color LIGHT_GREEN = Color.rgb(0, 255, 51);
    /**
     * Create a new Chromacystis.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Chromacystis(Field field, Location location, Color col) {
        super(field, location, col);
    }

    /**
    * This is how the Chromacystis decides if it's alive or not
    */
    public void act() {
        neighbours = getField().getLivingNeighbours(getLocation(), Chromacystis.class); // updates neighbour list
        updateInfectionState();
        
    
        if (isAlive() && !isInfected()) {
            updateColor();
            
            if (neighbours.size() == 1) {
                setNextState(true);
            }
            else if (neighbours.size() == 2) {
                setNextState(true);
            }
            else if (neighbours.size() == 3) {
                setNextState(true);
            }
            else {
                setNextState(false);
            }
        }
        else if(!isAlive() && !isInfected()) {
            setNextState(false);
        }
    }
    
    /**
     * Certain conditions are checked to determine if the cell can be revived
     * If the Chromacystis cell has 3 cells adjacent to it, it can be revived
     * @return True if the cell can be revived
     */
    public boolean canRevive(){
        neighbours = getField().getLivingNeighbours(getLocation(), Chromacystis.class); // updates neighbour list
        if(neighbours == null || getKilled()) {
            return false;
        }
        else {
            return neighbours.size() == 3;
        }
    }
    
    /**
     * Updates the color of the cell based on its neighbour size
     */
    public void updateColor() {
        neighbours = getField().getLivingNeighbours(getLocation(), Chromacystis.class);
        if (neighbours.size() == 1) {
            setColor(Color.BLUE);
        }
        else if (neighbours.size() == 2) {
            setColor(LIGHT_GREEN);
        }
        else if (neighbours.size() == 3) {
            setColor(Color.RED);
        }
    }
}
