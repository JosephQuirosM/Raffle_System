package classes;

public class Raffle {
    private int id;
    private String description;
    private String prize;
    private int price;
    private String date;
    private int numbers;

    public Raffle(int id, String description, String prize, int price, String date, int numbers) {
        this.id = id;
        this.description = description;
        this.prize = prize;
        this.price = price;
        this.date = date;
        this.numbers = numbers;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPrize() {
        return prize;
    }

    public int getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public int getNumbers() {
        return numbers;
    }
    
    
    
}
