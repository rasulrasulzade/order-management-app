public class Share implements Comparable<Share>{
    private  int price;
    private  int size;
    private  String type;

    public Share(int price, int size, String type) {
        this.price = price;
        this.size = size;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Share{" +
                "price=" + price +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(Share other) {
        return this.price > other.getPrice() ? 1 : -1;
    }
}
