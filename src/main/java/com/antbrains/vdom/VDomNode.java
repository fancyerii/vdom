package com.antbrains.vdom;

import java.util.ArrayList;
import java.util.Map;

public class VDomNode {
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Map<String, String> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}
	public ArrayList<VDomNode> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<VDomNode> children) {
		this.children = children;
	}
	public VDomNode getParent() {
		return parent;
	}
	public void setParent(VDomNode parent) {
		this.parent = parent;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public double getLeft() {
		return left;
	}
	public void setLeft(double left) {
		this.left = left;
	}
	public double getTop() {
		return top;
	}
	public void setTop(double top) {
		this.top = top;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	private double left;
	private double top;
	private double width;
	private double height;
	private int level;
	private int fontSize;
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	private String tag;
	private Map<String,String> attrs;
	private ArrayList<VDomNode> children=new ArrayList<>();
	private VDomNode parent;
	private int type;
	private String text;
	
	public static String printTree(VDomNode root){
		StringBuilder sb=new StringBuilder();
		recurPrintTree(root, 0, sb);
		return sb.toString();
	}
	
	private static void recurPrintTree(VDomNode curNode,int indent,StringBuilder sb){
		if(curNode.tag!=null){//element
			for(int i=0;i<indent;i++){
				sb.append("\t");
			}
			sb.append("<"+curNode.tag+"["+curNode.left+","+curNode.top+","+curNode.width+","+curNode.height+"]>").append("\n");
			
			for(VDomNode child:curNode.children){
				recurPrintTree(child, indent+1, sb);
			}
			
			for(int i=0;i<indent;i++){
				sb.append("\t");
			}
			sb.append("</"+curNode.tag+">").append("\n");
		}else{//text
			for(int i=0;i<indent;i++){
				sb.append("\t");
			}
			sb.append("["+curNode.left+","+curNode.top+","+curNode.width+","+curNode.height+"]"+"["+curNode.fontSize+"px]"+curNode.text).append("\n");
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder("");
		if(tag!=null){
			for(int i=0;i<level;i++){
				sb.append("\t");
			}
			sb.append("<"+tag+"["+left+","+top+","+width+","+height+"]>").append("\n");
		}else{
			for(int i=0;i<level;i++){
				sb.append("\t");
			}
			
			sb.append("["+left+","+top+","+width+","+height+"]"+"["+fontSize+"px]"+text).append("\n");
		}
		
		return sb.toString();
	}
	
	public static double[] getContainingRect(VDomNode root){
		double[] rect=new double[4];
		recurGetContainingRect(root, rect);
		return rect;
	}

	private static void recurGetContainingRect(VDomNode curNode, double[] rect){
		if(curNode.width>0 && curNode.height>0){
			if(rect[0]>curNode.left){
				rect[0]=curNode.left;
			}
			if(rect[1]>curNode.top){
				rect[1]=curNode.top;
			}
			
			if(rect[2]<curNode.left+curNode.width){
				rect[2]=curNode.left+curNode.width;
			}
			if(rect[3]<curNode.top+curNode.height){
				rect[3]=curNode.top+curNode.height;
			}
		}
		
		if(curNode.tag!=null){
			for(VDomNode child:curNode.children){
				recurGetContainingRect(child, rect);
			}
		}
	}
}
