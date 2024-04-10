package classes;

import java.time.LocalDate;

public class Raffle
{
    // attributes
   private String description;
    private String prize;
    private int price;
    private LocalDate date;
    private int totalNums;
  
    //Constructors
    public Raffle(String description, String prize, int price, LocalDate date, int totalNums) {
        this.description = description;
        this.prize = prize;
        this.price = price;
        this.date = date;
        this.totalNums = totalNums;
    }
    //public methods
    
    //private methods

    
}
