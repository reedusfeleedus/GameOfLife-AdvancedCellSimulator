 

import javafx.scene.paint.Color; 
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the shared characteristics of all forms of life
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2022.01.06
 */

public abstract class Cell {
    protected List<Cell> neighbours;
    private Field field;
    private Location location;
    
    // attributes for disease challenge task
    private int infectedDuration;
    private boolean infected;
    private boolean canSpread;
    private boolean killed; // when a cell is killed, it cannot be revived back to its same species (e.g A chromacystis cell that was killed by a Phasophyta cannot revive as chromacystis)
    private static final double SPREAD_PROB = 0.15;
    
    private boolean alive;    
    private boolean nextAlive; // The state of the cell in the next iteration
    
    // attributes to set color
    private Color color = Color.WHITE;
    private static final Color LIGHT_GRAY = Color.rgb(160,160,160);
    private static final Color DARK_GRAY = Color.rgb(96,96,96);

    /**
     * Create a new cell at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Cell(Field field, Location location, Color col) {
        alive = true;
        nextAlive = false;
        infected = false;
        this.field = field;
        setLocation(location);
        setColor(col);
        infectedDuration = 0;
    }

    /**
     * Make this cell act - that is: the cell decides it's status in the
     * next generation.
     */
    abstract public void act();
    
    /**
     * Checks whether the cell should or shouldnt be able to come back alive.
     */
    abstract public boolean canRevive();
    
    /**
     * Updates the cell's disease infection state.
     * After some time, cell might spread disease to neighbouring cells
     */
    protected void updateInfectionState() {
        if(isInfected() && isAlive()) {
            infectedDuration++;
            spreadDisease(); // tries to spread disease
            setNextState(true);
            
            if(infectedDuration >= 5 && infectedDuration <= 10) {
                setColor(DARK_GRAY);
                canSpread = true; // cell can now spread disease after 5 generations
            }
            else if(infectedDuration > 10) {
                setKilled(); // cell is killed by disease
                setNextState(false);
            }
        }
    }
    
    /**
     * Tries to spread disease to neighbouring cell
     */
    protected void spreadDisease() {
        if(canSpread) {
            for(Cell cell: neighbours) {
                if(Math.random() <= SPREAD_PROB) {
                    cell.setInfected();
                }
            }
        }
    }
    
    /**
     * Indicate that the cell is infected with disease
     */
    public void setInfected() {
        if(!infected) {
            infected = true;
            setColor(LIGHT_GRAY);
        }
    }
    
    /**
     * Checks whether the cell was killed.
     */
    public boolean getKilled() {
        return killed;
    }
    
    /**
     * Indicate that the cell was killed.
     */
    public void setKilled() {
        killed = true;
    }
    
    /**
     * Check whether the cell is infected or not.
     * @return true if the cell is infected.
     */
    protected boolean isInfected() {
        return infected;
    }

    /**
     * Check whether the cell is alive or not.
     * @return true if the cell is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }
    
    /**
     * Indicate that the cell will be alive or dead in the next generation.
     */
    public void setNextState(boolean value) {
        nextAlive = value;
    }

    /**
     * Changes the state of the cell
     */
    public void updateState() {
        alive = nextAlive;
    }
    
    /**
     * Changes the color of the cell
     */
    public void setColor(Color col) {
        color = col;
    }

    /**
     * Returns the cell's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the cell's location.
     * @return The cell's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the cell at the new location in the given field.
     * @param location The cell's location.
     */
    protected void setLocation(Location location) {
        this.location = location;
        field.place(this, location);
    }

    /**
     * Return the cell's field.
     * @return The cell's field.
     */
    protected Field getField() {
        return field;
    }
}
