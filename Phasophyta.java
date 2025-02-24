 

import javafx.scene.paint.Color; 
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Phasophyta's survival and reproducton thresholds decrease as it progresses through generations, simulating aging or a lifecycle.
 * This cell has a parasitic symbiotic relation with Chromacystis. It kills chromacystis to restore its own age (Benefits from relation)
 * =================================================================================================================================
 * Rules
 * Young (Ages 1-4): needs 2 or more neigbours to survive and cannot reproduce.
 * Mature(Ages 5-14): needs 1 or more neighbous to survive, and 2 to reproduce
 * Old (Ages 15-20) : survives alone but cannot reproduce, reflecting an end-of-life stage. Dies at 21st generation
 * 
**/
public class Phasophyta extends Cell {
    private int age;
    private boolean reproduce;
    
    private List<Cell> chromaCells;
    private Map<Chromacystis, Integer> durationMap;
    
    public static final Color LIGHT_PURPLE = Color.rgb(255, 204, 255);
    public static final Color PURPLE = Color.rgb(255, 0, 255);
    public static final Color DARK_PURPLE = Color.rgb(153, 0, 153);
    
    /**
     * Create a new Phasophyta.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Phasophyta(Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        durationMap = new HashMap<>();
    }
    
    /**
    * This is how the Phasophyta decides if it's alive or not
    */
    public void act() {
        age++;
        neighbours = getField().getLivingNeighbours(getLocation(), Phasophyta.class); // updates neighbour list
        updateInfectionState();
        updateDuration();
        restoreAge();
        reproduce = false;
        
    
        if (isAlive() && !isInfected()) {
            updateColor();
            
            if (age >= 1 && age <= 4) {
                if (neighbours.size() >= 2) {
                    setNextState(true); 
                }
            }
            
            else if (age >= 5 && age <= 14) {
                reproduce = true;
                
                if (neighbours.size() >= 1) {
                    setNextState(true); 
                }
            }
            
            else if (age >= 15 && age <= 20) {
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
     * Updates the color of the cell based on its age
     */
    private void updateColor() {
        if (age >= 1 && age <= 4) {
            setColor(LIGHT_PURPLE);
        }
        else if (age >= 5 && age <= 14) {
            setColor(PURPLE);
        }
        else if (age >= 15 && age <= 20) {
            setColor(DARK_PURPLE);
        }
    }
    
    /**
     * @return True if the cell can reproduce
     */
    public boolean canReproduce() {
        return reproduce;
    }
    
    /**
     * Certain conditions are checked to determine if the cell can be revived
     * If the Phasophyta cell has 2 mature cells adjacent to it, it can be revived
     * @return True if the cell can be revived
     */
    public boolean canRevive(){
        neighbours = getField().getLivingNeighbours(getLocation(), Phasophyta.class);
        int matureNeighbours = 0;
        if(neighbours != null) {
            for(Cell cell: neighbours) {
                if (((Phasophyta) cell).canReproduce()) {
                    matureNeighbours++;
                }
            }
        }
        return matureNeighbours == 2;
    }
    
    /**
     * Updates the map storing the duration of symbiotic relation between the cell and each
     * of its Chromacystis neighbours.
     */
    private void updateDuration() {
        chromaCells = getField().getLivingNeighbours(getLocation(), Chromacystis.class); // list of neighbouring chromacystis cells (victims)
        
        if(chromaCells.size() > 0) {
            for(Cell chroma: chromaCells) {
                Integer duration = durationMap.get(chroma);
                durationMap.put((Chromacystis)chroma, duration == null ? 1: duration + 1);
            }
        }
        
    }
    
    /**
     * Attempts to restore the cell's age
     * Cell's age decreases by 2 for each successful attempt
     */
    private void restoreAge() {
        if(durationMap.keySet() != null) {
            
            List<Cell> cellsToRemove = new ArrayList<>();
            for(Chromacystis chroma: durationMap.keySet()) {
                Integer duration = durationMap.get(chroma);
                
                if(duration >= 3) {
                    age = age - 2;
                    chroma.setNextState(false);
                    chroma.setKilled();
                    cellsToRemove.add(chroma);
                }
            }
            
            durationMap.keySet().removeAll(cellsToRemove);
        }
    }
}
