import javafx.scene.paint.Color; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.lang.reflect.Constructor;


/**
 * A Life (Game of Life) simulator, first described by British mathematician
 * John Horton Conway in 1970.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2024.02.03
 */

public class Simulator {
    private List<Cell> cells;
    private Field field;
    private int generation;
    
    private static final double DISEASE_PROB = 0.05; // cells have a 5% chance to spawn infected with disease
    private static final double CELL_ALIVE_PROB = 0.25;
    
    public static final Color LIGHT_PURPLE = Color.rgb(255, 180, 255);
    public static final Color DARK_CYAN = Color.rgb(0, 225, 225);
    public static final Color CYAN = Color.rgb(100, 255, 255);

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(SimulatorView.GRID_HEIGHT, SimulatorView.GRID_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        cells = new ArrayList<>();
        field = new Field(depth, width);
        reset();
    }

    /**
     * Run the simulation from its current state for a single generation.
     * Iterate over the whole field updating the state of each life form.
     */
    public void simOneGeneration() {
        generation++;
        List<Cell> deadCells = new ArrayList<>();
        List<Cell> revivedCells = new ArrayList<>();
        for (Iterator<Cell> it = cells.iterator(); it.hasNext(); ) {
            Cell cell = it.next();
            
            cell.act(); // determines the cell's next state
            
            if(!cell.isAlive()) {
                
                Cell newCell = revive(cell); // tries to revive a dead cell
                
                if(newCell != null) {
                    revivedCells.add(newCell);
                    deadCells.add(cell);
                    newCell.setNextState(true);
                }
                
            }
        }
        
        // updates the list of cells 
        cells.addAll(revivedCells);
        cells.removeAll(deadCells);

        for (Cell cell : cells) {
          cell.updateState();
        }
    }
    
    /**
     * Tries to revive a dead cell, does nothing if conditions are not satisfied
     * @param cell The cell being revived
     * @return The new cell produced
     */
    public Cell revive(Cell cell) {
        List<Cell> neighbours = getField().getLivingNeighbours(cell.getLocation()); // gets a list of living neighbours, which includes all species
        Cell newCell = null;
        Cell tempCell = null;
        
        if(neighbours == null) {
            return null;
        }
        
        // loops through neighbouring cells until the cell is revived
        for (int i=0; i<neighbours.size() && newCell == null; i++) {
            Cell neighbourCell = neighbours.get(i);
            
            // checks for neighbouring cell species
            // checks if the reviving cell is this species
            // if the previous statement is true, ensure that it was not killed
            if (neighbourCell instanceof Mycoplasma &&
                    !(cell instanceof Mycoplasma && cell.getKilled())) { 
                tempCell = new Mycoplasma(field, cell.getLocation(), Color.ORANGE);
            }
            
            else if (neighbourCell instanceof Phasophyta &&
                    !(cell instanceof Phasophyta && cell.getKilled())) {
                tempCell = new Phasophyta(field, cell.getLocation(), LIGHT_PURPLE);
            }
            
            else if (neighbourCell instanceof Chromacystis &&
                    !(cell instanceof Chromacystis && cell.getKilled())) {
                tempCell = new Chromacystis(field, cell.getLocation(), Color.YELLOW);
            }
            
            else if (neighbourCell instanceof Polycephalum &&
                    !(cell instanceof Polycephalum && cell.getKilled())) {
                tempCell = new Polycephalum(field, cell.getLocation(), DARK_CYAN);
            }
            
            if(tempCell != null && tempCell.canRevive()) {
                newCell = tempCell; 
            }
            else if(tempCell != null && !tempCell.canRevive()) {
                tempCell.setNextState(false);
                tempCell.updateState();
            }
        }
        
        return newCell;
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        generation = 0;
        cells.clear();
        populate();
    }

    /**
     * Randomly populate the field live/dead life forms
     */
    private void populate() {
      Random rand = Randomizer.getRandom();
      field.clear();
      for (int row = 0; row < field.getDepth(); row++) {
        for (int col = 0; col < field.getWidth(); col++) {
          Location location = new Location(row, col);
          
          Random random = new Random();
          int chooseOrganism = random.nextInt(4);
          Cell cell = null; 
          if (chooseOrganism == 0) {
              cell = new Mycoplasma(field, location, Color.rgb(255, 165, 0));
            }
          else if (chooseOrganism == 1) {
              cell = new Chromacystis(field, location, Color.rgb(50, 150, 255));
            }
          else if (chooseOrganism == 2) {
              cell = new Phasophyta(field, location, LIGHT_PURPLE);
          }
          else if (chooseOrganism == 3) {
              cell = new Polycephalum(field, location, DARK_CYAN);
          }
          
          if(rand.nextDouble() <= DISEASE_PROB) {
              cell.setInfected();
          }
          
          if (rand.nextDouble() <= CELL_ALIVE_PROB) {
            cells.add(cell);
          }
          else {
            cell.setNextState(false);
            cell.updateState();
            cells.add(cell);
          }
        }
      }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
    public Field getField() {
        return field;
    }

    public int getGeneration() {
        return generation;
    }
}
