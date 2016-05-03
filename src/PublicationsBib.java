
public class PublicationsBib 
{

	public static String toBib(Publication p)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<body>" + "\n");
		
		if ("C".equals(p.type))
		{
			sb.append("@inproceedings{");
		}
		else if ("J".equals(p.type))
		{
			
		}
		else if ("B".equals(p.type))
		{
			
		}
		else
		{
			
		}
		
		//sb.append("\n");
		
		sb.append(p.id + ", <br />" + "\n");
		sb.append("&nbsp;&nbsp;" + "title={" + p.title + "}, <br />" + "\n");
		sb.append("&nbsp;&nbsp;" + "author={" + p.authors_bib + "}, <br />" + "\n");
		sb.append("&nbsp;&nbsp;" + "booktitle={" + p.venue + "}, <br />" + "\n");
		sb.append("&nbsp;&nbsp;" + "year={" + p.year + "} <br />" + "\n");
		
		sb.append("\n");
		
		sb.append("}" + "\n");
		
		sb.append("</body>" + "\n");
		
		
		return sb.toString();
	}

}
