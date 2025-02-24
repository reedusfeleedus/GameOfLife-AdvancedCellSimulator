 

import javafx.scene.paint.Color; 
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Polycephalum is a non-deterministic micro-organism
 * ========================================================================================
 * Rules
 * Survival:
 *  a) 20% of the time it survives only if it has 1 neighbour
 *  b) 30% of the time it survives only if it has 2 neighbours
 *  c) 50% of the time it survives only if it has 3 neighbours
 * Colour change: 65% of the time it changes colour
 * Revival: 
 *  a) 75% of the time it revives if it has exactly 2 neighbours
 *  b) 25% of the time it cannot revive regardless of condition
 */

public class Polycephalum extends Cell {
    public static final Color DARK_CYAN = Color.rgb(0, 204, 204);
    public static final Color CYAN = Color.rgb(0, 255, 255);
    
    private static final double CHANGE_COLOUR_PROBABILITY = 0.65;
    
    /**
     * Create a new Polycephalum.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Polycephalum(Field field, Location location, Color col) {
        super(field, location, col);
    }

    /**
    * This is how the Polycephalum decides if it's alive or not
    */
    public void act() {
        neighbours = getField().getLivingNeighbours(getLocation(), Polycephalum.class); // updates neighbour list
        updateInfectionState();
        Random rand = Randomizer.getRandom();
        double probability;
        
        if(isAlive() && !isInfected()) {
            setNextState(false);
            
            probability = rand.nextDouble();
            if(probability <= 0.20) {
                if(neighbours.size() == 1) {
                    setNextState(true); // 20% of the time it survives if it has 1 neighbour
                }
            }
            
            else if(probability <= 0.50) {
                if(neighbours.size() == 2) {
                    setNextState(true); // 30% of the time it survives only if it has 2 neighbours
                }
            }
            
            else {
                if(neighbours.size() == 3) {
                    setNextState(true); // 50% of the time it survives only if it has 3 neighbours
                }
            }
            
            probability = rand.nextDouble();
            if(probability <= CHANGE_COLOUR_PROBABILITY) {
                updateColor();
                setNextState(true);
            }
        }
        else if(!isAlive() && !isInfected()) {
            setNextState(false);
        }
    }
    
    public boolean canRevive() {
        neighbours = getField().getLivingNeighbours(getLocation(), Mycoplasma.class); // updates neighbour list
        Random rand = Randomizer.getRandom();
        
        if(rand.nextDouble() <= 75) {
            return (neighbours.size() == 2); // 75% of the time it revives if it has exactly 2 neighbours
        }
        
        else {
            return false; // 25% of the time it cannot revive regardless of the conditions
        }
    }
    
    private void updateColor() {
        if(getColor().equals(DARK_CYAN)) {
            setColor(CYAN);
        }
        else if(getColor().equals(CYAN)) {
            setColor(DARK_CYAN);
        }
    }
}

    
    
