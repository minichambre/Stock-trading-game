import java.util.Arrays;

public class Request {

	public RequestType type;
	public String[] params;

	public Request(RequestType type, String... params) {
		this.type = type;
		this.params = params;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(type); 
		for (int i = 0; i < params.length; i++)
		sb.append(" " + params[i]); 
		return sb.toString(); 
	}

	public static Request parse(String line) {
		try {
			String[] items = line.trim().split("\\s+");
			switch (items[0].toUpperCase()) {
			case "TRADE":
				return new Request(RequestType.TRADE, items[1], items[2], items[3]);
			case "GETMONEY":
				return new Request(RequestType.GETMONEY);
			case "VOTE":
				return new Request(RequestType.VOTE, items[1], items[2]);
			case "GETCARDS":
				return new Request(RequestType.GETCARDS);
			case "GETPRICES":
				return new Request(RequestType.GETPRICES);
			case "GETSHARES" :
				return new Request(RequestType.GETSHARES);
			case "GETWINNER" :
				return new Request(RequestType.GETWINNER);
			case "QUIT":
			case "LOGOUT": 
				return new Request(RequestType.LOGOUT);
			default:
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return new Request(RequestType.INVALID, line);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (!Arrays.equals(params, other.params))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
