package view;

/**
 * Standard View Interface for CLI Presentation Layer.
 * Defines the contract for all interactive and presentation views in the system.
 * Part of the MVC (Model - View - Controller) architecture adhering to OOP Abstraction.
 */
public interface IView {

    /**
     * Renders or initiates the interactive display for this view.
     */
    void display();
}
