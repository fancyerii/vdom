package com.antbrains.vdom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx4j.log.Logger;

public class TreeParser {
	public static VDomNode parse(String s) throws Exception{
		if(s==null) return null;
		
		VDomNode root=new VDomNode();
		root.setTag("ROOT");
		root.setLevel(-1);
		String[] lines=s.split("\n");
		VDomNode next=root;
		
		for(int i=0;i<lines.length;i++){
			next=parseLine(lines, i, next, next.getLevel());
			if(next==null){
				System.err.println("line: "+i);
			}
		}
		return root;
	}
	private static Pattern elemPtn1=Pattern.compile("1\\[([^,]+),([^,]+),([^,]+),([^]]+)\\]<([^>]+)>");
	private static Pattern elemPtn2=Pattern.compile("1</([^>]+)>");
	private static Pattern elemPtn3=Pattern.compile("0\\[([^,]+),([^,]+),([^,]+),([^]]+)\\]\\[([^]]+)\\](.*)");
	private static VDomNode parseLine(String[] lines, int lineNum, VDomNode parent, int indent) throws Exception{
		String line=lines[lineNum];
		int myIndent=getIndentCount(line);
		line=line.substring(myIndent);
		if(line.trim().equals("")){
			return parent;
		}
		
		Matcher m1=elemPtn1.matcher(line);
		if(m1.matches()){//start element
			double left=Double.valueOf(m1.group(1));
			double top=Double.valueOf(m1.group(2));
			double width=Double.valueOf(m1.group(3));
			double height=Double.valueOf(m1.group(4));
			String tagName=m1.group(5);
			VDomNode node=new VDomNode();
			node.setTag(tagName);
			node.setLeft(left);
			node.setTop(top);
			node.setWidth(width);
			node.setHeight(height);
			
			parent.getChildren().add(node);
			node.setParent(parent);
			node.setLevel(indent+1);
			return node;
		}else{
			Matcher m2=elemPtn2.matcher(line);
			if(m2.matches()){//end element
				return parent.getParent();
			}else{//text node
				Matcher m3=elemPtn3.matcher(line);
				if(m3.matches()){
					double left=Double.valueOf(m3.group(1));
					double top=Double.valueOf(m3.group(2));
					double width=Double.valueOf(m3.group(3));
					double height=Double.valueOf(m3.group(4));
					String fontSize=m3.group(5);

					String text=m3.group(6);
					VDomNode node=new VDomNode();
					node.setLeft(left);
					node.setTop(top);
					node.setWidth(width);
					node.setHeight(height);
					node.setText(text);
					parent.getChildren().add(node);
					node.setParent(parent);
					node.setLevel(indent+1);
					
					if(fontSize.endsWith("px")){
						try{
							int fs=Integer.valueOf(fontSize.substring(0,fontSize.length()-2));
							node.setFontSize(fs);
						}catch(Exception e){
							System.err.println("fontsize error: "+fontSize);
						}
					}
					
					return parent;
				}else{
					throw new Exception("line: "+lineNum+" parse error: "+lines[lineNum]);
				}

			}
		}
		
	}
	
	private static int getIndentCount(String s){
		int count=0;
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			if(ch=='\t'){
				count++;
			}else{
				break;
			}
		}
		
		return count;
	}
	
	public static void main(String[] args) throws Exception{
		String path="testtree";
		InputStream is=TestPhantomjs2.class.getClassLoader().getResourceAsStream(path);
		BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF8"));
		StringBuilder sb=new StringBuilder();
		String line;
		while((line=br.readLine())!=null){
			sb.append(line).append("\n");
		}
		br.close();
		String tree=sb.toString();
		VDomNode root=TreeParser.parse(tree);
		System.out.println(VDomNode.printTree(root));
	}
}
