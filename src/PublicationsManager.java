import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class PublicationsManager 
{
	static int COUNT_C = 0;    //conference & workshop
	static int COUNT_J = 0;    //journal
	static int COUNT_B = 0;    //book charpter & book
	static int COUNT_O = 0;    //other
	
	public static void main(String[] args) throws Exception 
	{	
		Workbook wb = WorkbookFactory.create(new File("src/li-publications.xlsx"));
		Sheet sheet = wb.getSheetAt(0);
		Map<Integer, List<Publication>> publications = obtainPublications(sheet);
		toFullList(publications, "publications.html");
		toBib(publications, "bibs");
	}
	
	public static void toFullList(Map<Integer, List<Publication>> publications, String outputFilePath)
	{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		System.out.println(year);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>" + "\n");
		sb.append("  <head>" + "\n");
		sb.append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\">" + "\n");
		sb.append("    <meta charset=\"UTF-8\">" + "\n");
		sb.append("    <title>Li Li's Publications</title>" + "\n");
		sb.append("  </head>" + "\n");
		sb.append("  <body>" + "\n");
		
		
		sb.append("    <nav>" + "\n");
		sb.append("    <ul>" + "\n");
		sb.append("      <li><a href=\"index.html\"><font size=\"4\">Home</font></a></li>" + "\n");
		//sb.append("      <li><a href=\"news.html\"><font size=\"4\">News</font></a></li>" + "\n");
		sb.append("      <li><a href=\"publications.html\"><font size=\"4\">Publications</font></a></li>" + "\n");
		sb.append("      <li><a href=\"team.html\"><font size=\"4\">Team</font></a></li>" + "\n");
		sb.append("      <li><a href=\"services.html\"><font size=\"4\">Services</font></a></li>" + "\n");
		sb.append("      <li><a href=\"awards.html\"><font size=\"4\">Awards</font></a></li>" + "\n");
		sb.append("      <li><a href=\"artefacts.html\"><font size=\"4\">Artefacts</font></a></li>" + "\n");
		sb.append("    </ul>" + "\n");
		sb.append("    </nav>" + "\n");
		
		
		
		for (int i = (year+1); i >= 2014; i--)
		{
			List<Publication> yearPublications = publications.get(i);
			if (null == yearPublications || yearPublications.size() == 0)
			{
				continue;
			}
			
			 
			sb.append("    <p>" + "\n");
			sb.append("      <h3>" + i + "</h3>" + "\n");
			
			sb.append("      <ul>" + "\n");
			
			for (int j = yearPublications.size(); j > 0; j--)
			{
				Publication p = yearPublications.get(j-1);
				
				sb.append("        <li>" + "\n");
				sb.append("          " + "[" + p.count + "]&nbsp;" + "\n");
				sb.append("          " + p.authors.replace("Li Li", "<b>Li Li</b>") + ", " + p.title + ", " + p.venue + ", " + p.year + "\n");
				sb.append("          " + "&nbsp;<a href=\"papers/" + p.id + ".pdf" + "\">[pdf]</a>" + "\n");
				sb.append("          " + "&nbsp;<a href=\"bibs/" + p.id + ".html" + "\">[bib]</a>" + "\n");
				
				if (null != p.www)
				{
					sb.append("          " + "&nbsp;<a href=\"" + p.www + "\">[www]</a>" + "\n");
				}
				
				if (null != p.technicalReport)
				{
					sb.append("          " + "&nbsp;<a href=\"papers/" + p.technicalReport + ".pdf" + "\">[technical-report]</a>" + "\n");
				}
				
				if (null != p.notes)
				{
					sb.append("          " + "&nbsp;[<b>" + p.notes + "</b>]" + "\n");
				}
				
				sb.append("        </li>" + "\n");
			}
			
			sb.append("      </ul>" + "\n");
			sb.append("    </p>" + "\n");
		}
		
		sb.append("  </body>" + "\n");
		sb.append("</html>" + "\n");
		
		System.out.println(sb.toString());
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, false)));
		    out.print(sb.toString());
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static void toBib(Map<Integer, List<Publication>> publications, String dir)
	{
		for (Map.Entry<Integer, List<Publication>> entry : publications.entrySet())
		{
			//int year = entry.getKey();
			List<Publication> yearPublications = entry.getValue();
			
			
			for (Publication p : yearPublications)
			{
				String bibDest = dir + "/" + p.id + ".html";
				File file = new File(bibDest);
				
				if (file.exists())
				{
					System.out.println("====>" + bibDest + " exists already!");
					continue;
				}
				else
				{
					String bib = PublicationsBib.toBib(p);
					System.out.println(bib);
					
					try {
					    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(bibDest, false)));
					    out.print(bib);
					    out.close();
					} catch (IOException e) {
					    e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static Map<Integer, List<Publication>> obtainPublications(Sheet sheet)
	{
		Map<Integer, List<Publication>> publications = new HashMap<Integer, List<Publication>>();
		
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++)
		{
			Row row = sheet.getRow(rowNum);
		
			if (null == row)
				break;
			
			Publication p = new Publication();
			p.id = row.getCell(0).getStringCellValue().trim();
			
			/*
			 * Selected Feature is Deprecated!
			if (null != row.getCell(1))
			{
				String c1 = row.getCell(1).getStringCellValue();
				if (c1.equals("X"))
				{
					p.selected = true;
				}
				else
				{
					p.selected = false;
				}
			}*/
			
			p.type = row.getCell(2).getStringCellValue().trim();
			
			if ("C".equals(p.type))
			{
				COUNT_C++;
				p.count = "C" + COUNT_C;
			}
			else if ("J".equals(p.type))
			{
				COUNT_J++;
				p.count = "J" + COUNT_J;
			}
			else if ("B".equals(p.type))
			{
				COUNT_B++;
				p.count = "B" + COUNT_B;
			}
			else
			{
				COUNT_O++;
				p.count = "O" + COUNT_O;
			}
			
			p.year = (int) row.getCell(3).getNumericCellValue();
			p.authors = row.getCell(4).getStringCellValue().trim();
			p.authors_bib = row.getCell(5).getStringCellValue().trim();
			p.title = row.getCell(6).getStringCellValue().trim();
			p.venue = row.getCell(7).getStringCellValue().trim();
			
			if (null != row.getCell(8))
			{
				p.www = row.getCell(8).getStringCellValue().trim();
			}
			
			if (null != row.getCell(9))
			{
				p.technicalReport = row.getCell(9).getStringCellValue().trim();
			}
			
			if (null != row.getCell(10))
			{
				p.notes = row.getCell(10).getStringCellValue().trim();
			}
		
			if (publications.containsKey(p.year))
			{
				List<Publication> papers = publications.get(p.year);
				papers.add(p);
				publications.put(p.year, papers);
			}
			else
			{
				List<Publication> papers = new ArrayList<Publication>();
				papers.add(p);
				publications.put(p.year, papers);
			}
			
			System.out.println(p);
		}
		
		return publications;
	}
}

class Publication
{
	public String id;
	public boolean selected;
	public String type;
	public int year;
	public String authors;
	public String authors_bib;
	public String title;
	public String venue;
	public String www;
	public String technicalReport;
	public String notes;
	
	public String count;
	
	@Override
	public String toString() {
		return "Publication [id=" + id + ", selected=" + selected + ", type="
				+ type + ", year=" + year + ", authors=" + authors + ", authors_bib=" + authors_bib + ", title="
				+ title + ", venue=" + venue + ", www=" + www
				+ ", technicalReport=" + technicalReport + ", notes=" + notes
				+ "]";
	}
}
