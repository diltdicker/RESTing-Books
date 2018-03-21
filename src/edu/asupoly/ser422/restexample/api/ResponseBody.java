package edu.asupoly.ser422.restexample.api;

import java.util.ArrayList;
import java.util.List;

public class ResponseBody {

	private String body;
	private boolean isXML;
	private List<LinkValue> hyperMediaLinks;
	private final String _XMLHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public ResponseBody() {
		body = "";
		isXML = false;
		hyperMediaLinks = new ArrayList<LinkValue>();
	}
	
	public ResponseBody(boolean isXML) {
		this.isXML = isXML;
		body = "";
		hyperMediaLinks = new ArrayList<LinkValue>();
	}
	
	public ResponseBody(boolean isXML, String body) {
		this.isXML = isXML;
		this.body = body;
		hyperMediaLinks = new ArrayList<LinkValue>();
	}
	
	public void addLinkValue(String key, String value) {
		hyperMediaLinks.add(new LinkValue(key, value));
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isXML() {
		return isXML;
	}

	public List<LinkValue> getHyperMediaLinks() {
		return hyperMediaLinks;
	}

	public void setHyperMediaLinks(List<LinkValue> hyperMediaLinks) {
		this.hyperMediaLinks = hyperMediaLinks;
	}

	public void setXML(boolean isXML) {
		this.isXML = isXML;
	}

	public String getResponseString(){
		String response = "";
		if (isXML) {
			response = _XMLHEADER;
			response += "\n<response>";
			response += "\n" + body;
			response += "<links>";
			for (int i = 0; i < hyperMediaLinks.size(); i++) {
				response += "\n" + "<" + hyperMediaLinks.get(i).key + ">" + hyperMediaLinks.get(i).value + "</" + hyperMediaLinks.get(i).key + ">";
			}
			response += "\n</links>";
			response += "\n</response>";
			return response;
		} else {
			response += "{";
			response += "\"body\": ";
			response += body + ", ";
			response += "\"links\": ";
			response += "{";
			for (int i = 0; i < hyperMediaLinks.size(); i++) {
				response += "\"" + hyperMediaLinks.get(i).key + "\": \"" + hyperMediaLinks.get(i).value + "\"";
				if (i != hyperMediaLinks.size() - 1) {
					response += ", ";
				}else {
					response += " ";
				}
			}
			response += "}";
			response += "}";
			return response;
		}
	}
	
	public String getPartialRepsonseString() {
		String response = "";
		if (isXML) {
			//response = _XMLHEADER;
			response += "<response>";
			response += "\n" + body;
			response += "<links>";
			for (int i = 0; i < hyperMediaLinks.size(); i++) {
				response += "\n" + "<" + hyperMediaLinks.get(i).key + ">" + hyperMediaLinks.get(i).value + "</" + hyperMediaLinks.get(i).key + ">";
			}
			response += "\n</links>";
			response += "\n</response>";
			return response;
		} else {
			response += "{";
			response += "\"body\": ";
			response += body + ", ";
			response += "\"links\": ";
			response += "{";
			for (int i = 0; i < hyperMediaLinks.size(); i++) {
				response += "\"" + hyperMediaLinks.get(i).key + "\": \"" + hyperMediaLinks.get(i).value + "\"";
				if (i != hyperMediaLinks.size() - 1) {
					response += ", ";
				}else {
					response += " ";
				}
			}
			response += "}";
			response += "}";
			return response;
		}
	}
	
	/*
	 * JSON only
	 */
	public static String getPartialArray(String innerBody) {
		String response = "";
		response += "[";
		response += innerBody;
		response +="]";
		return response;
	}
	
	public class LinkValue {
		
		public LinkValue(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String key;
		public String value;
	}
	
}
