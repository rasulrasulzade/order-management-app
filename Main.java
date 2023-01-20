import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final List<Share> shares = new ArrayList<>();

    public static Share getBestBid(List<Share> bids) {
        Share result = bids.get(0);
        for (Share share : bids) {
            if (share.getPrice() > result.getPrice())
                result = share;
        }
        return result;
    }

    public static Share getBestAsk(List<Share> asks) {
        Share result = asks.get(0);
        for (Share share : asks) {
            if (share.getPrice() < result.getPrice())
                result = share;
        }
        return result;
    }

    public static void buy(int size) {
        Optional<Share> opt = shares.stream()
                .filter(s -> s.getType().equals("ask") && s.getSize() > 0)
                .sorted().findFirst();

        if (opt.isPresent()) {
            Share share = opt.get();
            share.setSize(share.getSize() - size);
        }
    }

    public static void sell(int size) {
        List<Share> sortedList = shares.stream()
                .filter(s -> s.getType().equals("bid") && s.getSize() > 0)
                .sorted().collect(Collectors.toList());

        Share expensive = sortedList.get(sortedList.size() - 1);
        expensive.setSize(expensive.getSize() - size);
    }

    public static Share getBest(String type) {
        List<Share> filteredList = shares.stream()
                .filter(s -> s.getType().equals(type) && s.getSize() > 0)
                .collect(Collectors.toList());
        if (filteredList.isEmpty()) return null;
        return type.equals("bid") ? getBestBid(filteredList) : getBestAsk(filteredList);
    }

    public static int getSize(int price) {
        AtomicInteger size = new AtomicInteger();
        shares.stream().filter(s -> s.getPrice() == price).forEach(s ->
                size.addAndGet(s.getSize())
        );
        return size.get();
    }

    public static void main(String[] args) {

        List<String> outputContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                switch (parts[0]) {
                    case "u":
                        shares.add(new Share(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]));
                        break;
                    case "o":
                        if (parts[1].equals("sell")) {
                            sell(Integer.parseInt(parts[2]));
                        } else if (parts[1].equals("buy")) {
                            buy(Integer.parseInt(parts[2]));
                        } else throw new RuntimeException("Invalid argument!!!");
                        break;
                    case "q":
                        switch (parts[1]) {
                            case "best_bid":
                                Share bestBid = getBest("bid");
                                if (bestBid == null) throw new RuntimeException("Best bid not found!!!");
                                outputContent.add(bestBid.getPrice() + ", " + bestBid.getSize());
                                break;
                            case "best_ask":
                                Share bestAsk = getBest("ask");
                                if (bestAsk == null) throw new RuntimeException("Best ask not found!!!");
                                outputContent.add(bestAsk.getPrice() + ", " + bestAsk.getSize());
                                break;
                            case "size":
                                int size = getSize(Integer.parseInt(parts[2]));
                                outputContent.add(String.valueOf(size));
                                break;
                            default:
                                throw new RuntimeException("Invalid argument!!!");
                        }
                        break;
                    default:
                        throw new RuntimeException("Invalid argument!!!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        write(outputContent);
    }

    static void write(List<String> outputContent) {
        try {
            FileWriter writer = new FileWriter("output.txt");
            for(String str:outputContent){
                writer.write(str);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
