import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Player {
    private int cash;
    public String name;
    public int votes = 0;
    public int lastVote = 10;
    public Map<String, Integer> shares = new TreeMap<>();

    public Player(String name)
    {
        this.name = name;
        cash = 500;
        initialiseShares();
        provideRandomShares();
    }

    private void provideRandomShares()
    {
        //gives the user 10 random shares.
        Random r = new Random();
        int runtime = 0;
        for (int x = 0; x < 10; x++)
        {
            switch (r.nextInt(5))
            {
                case 0 :
                    modifyShares("Apple", 1, '+');
                    break;
                case 1 :
                    modifyShares("BP", 1, '+');
                    break;
                case 2 :
                    modifyShares("Cisco", 1, '+');
                    break;
                case 3:
                    modifyShares("Dell", 1, '+');
                    break;
                case 4 :
                    modifyShares("Ericsson", 1, '+');
                    break;
            }
            runtime++;
        }
    }

    public void modifyShares(String share, int value, char op)
    {
        if (shares.containsKey(share) == false)
        {
            //if share doesn't currently exist, just add it normally.
            shares.put(share, value);
        }
        else
        {
            //share already exists, get current value.
            int oldValue = shares.get(share);

            //check what program wishes to do to the value.
            if (op == '+'){shares.put(share, oldValue + value);}
            if (op == '-') {shares.put(share, oldValue - value);}
        }
    }

    private void initialiseShares()
    {
        modifyShares("Apple", 0, '+');
        modifyShares("BP", 0, '+');
        modifyShares("Cisco", 0, '+');
        modifyShares("Dell", 0, '+');
        modifyShares("Ericsson", 0, '+');
    }

    public int getCash()
    {
        return this.cash;
    }

    //method for changing users cash
    public void changeCash(char op ,int val)
    {
        if (op == '-'){this.cash -= val;}
        if (op == '+'){this.cash += val;}
    }
}
