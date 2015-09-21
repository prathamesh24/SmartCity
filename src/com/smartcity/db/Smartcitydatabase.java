package com.smartcity.db;

import java.util.ArrayList;

import com.smartcity.Blog;
import com.smartcity.beans.BlogItems;
import com.smartcity.beans.BuyItems;
import com.smartcity.beans.ContactsItems;
import com.smartcity.beans.NavDrawerItem;
import com.smartcity.beans.ServiceItems;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Smartcitydatabase extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "smartdb";
	private static int DATABASE_VERSION = 1;

	public Smartcitydatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_CONTACTS_TABLE = "CREATE TABLE Userdetails (ID INTEGER PRIMARY KEY, NAME TEXT,PH_NO  TEXT,EMAIL  TEXT,Region TEXT)";
		String CREATE_NEWS_TABLE = "CREATE TABLE News (ID INTEGER PRIMARY KEY,DESC TEXT)";
		String CREATE_YVideo_TABLE = "CREATE TABLE Video (ID INTEGER PRIMARY KEY,VideoID TEXT)";
		String CREATE_images = "CREATE TABLE SliderImages (ID INTEGER PRIMARY KEY,Images TEXT)";
		String CREATE_drawerdb = "CREATE TABLE DrawerItems (ID INTEGER PRIMARY KEY,Images TEXT,category TEXT)";
		String CREATE_Service = "CREATE TABLE Services (ID INTEGER PRIMARY KEY,category TEXT,Images TEXT,service TEXT)";
		String CREATE_Blogs = "CREATE TABLE Blogs (ID INTEGER PRIMARY KEY,Name TEXT,Blog TEXT,date TEXT,Region TEXT)";
		String CREATE_ProertyTable = "CREATE TABLE Property (ID INTEGER PRIMARY KEY,PropertyType TEXT,Image TEXT,title TEXT,location TEXT,Mobile TEXT,amount TEXT)";
		String CREATE_SmallBannerTable="CREATE TABLE SmallSliderImages (ID INTEGER PRIMARY KEY,Images TEXT)";
		String CREATE_ContactTable="CREATE TABLE ContactAPI (ID INTEGER PRIMARY KEY,Category TEXT,Name TEXT,Image TEXT,Adress TEXT,contactnumber TEXT,website TEXT)";
		//Image,title,location,Mobile,amount
		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_NEWS_TABLE);
		db.execSQL(CREATE_YVideo_TABLE);
		db.execSQL(CREATE_images);
		db.execSQL(CREATE_drawerdb);
		db.execSQL(CREATE_Service);
		db.execSQL(CREATE_Blogs);
		db.execSQL(CREATE_ProertyTable);
		db.execSQL(CREATE_SmallBannerTable);
		db.execSQL(CREATE_ContactTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS Userdetails");
		 
	     // Create tables again
	     onCreate(db);	
	}
	
	public Boolean insertUserData(String NAME,String PH_NO,String EMAIL,String Region) {
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("NAME", NAME);
		values.put("PH_NO", PH_NO);
		values.put("EMAIL", EMAIL);
		values.put("Region", Region);
		
		if(database.insert("Userdetails", null, values)>0)
			return true;
		else
			return false;
	}
	
	
	public int getUserData() {
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from Userdetails",null);
		int counter=0;
		cursor.moveToFirst();
		while(cursor.isAfterLast()==false){
			counter=1;
			cursor.moveToNext();
		}
		
		return counter;
	}

	public String getRegion() {
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from Userdetails",null);
		String Region = null;
		cursor.moveToFirst();
		while(cursor.isAfterLast()==false){
			Region=cursor.getString(cursor.getColumnIndex("Region"));
			cursor.moveToNext();
		}
		
		return Region;
	}
	
	public String getName() {
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from Userdetails",null);
		String Region = null;
		cursor.moveToFirst();
		while(cursor.isAfterLast()==false){
			Region=cursor.getString(cursor.getColumnIndex("NAME"));
			cursor.moveToNext();
		}
		
		return Region;
	}
	
	
	public String getNumber() {
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from Userdetails",null);
		String Region = null;
		cursor.moveToFirst();
		while(cursor.isAfterLast()==false){
			Region=cursor.getString(cursor.getColumnIndex("PH_NO"));
			cursor.moveToNext();
		}
		
		return Region;
	}
	
	public String getEmail() {
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from Userdetails",null);
		String Region = null;
		cursor.moveToFirst();
		while(cursor.isAfterLast()==false){
			Region=cursor.getString(cursor.getColumnIndex("EMAIL"));
			cursor.moveToNext();
		}
		
		return Region;
	}
	/**************** News Home page *******************/
	
	public boolean InsertNews(String news){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("DESC", news);
		
		if(database.insert("News", null, values)>0)
			return true;
		else {
			return false;
		}
	}
	
	public boolean CheckNews(String news){
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("Select * from News where DESC like '%"+news+"%'", null);
		
		int count=0;
		cursor.moveToFirst();
		
		while (cursor.isAfterLast()==false) {
			count=1;
			cursor.moveToNext();
		}
		
		if(count==1){
			return false;
		}else {
			return true;
		}
	}
	
	public ArrayList<String> getNews() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("Select * from News", null);
		ArrayList<String>news=new ArrayList<String>();
		
		cursor.moveToFirst();
		
		while (cursor.isAfterLast()==false) {
			news.add(cursor.getString(cursor.getColumnIndex("DESC")));
			cursor.moveToNext();
		}
		
		return news;
		
	}
	
	/**************** Video *******************/
	
	public boolean InsertVideos(String VideoID){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("VideoID", VideoID);
		
		if(database.insert("Video", null, values)>0)
			return true;
		else {
			return false;
		}
	}
	
	public boolean CheckVideoID(String VideoID){
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("Select * from Video where VideoID like '%"+VideoID+"%'", null);
		
		int count=0;
		cursor.moveToFirst();
		
		while (cursor.isAfterLast()==false) {
			count=1;
			cursor.moveToNext();
		}
		
		if(count==1){
			return false;
		}else {
			return true;
		}
	}
	
	public ArrayList<String> getAllVideos() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("Select * from Video", null);
		ArrayList<String>VideoIDs=new ArrayList<String>();
		
		cursor.moveToFirst();
		
		while (cursor.isAfterLast()==false) {
			VideoIDs.add(cursor.getString(cursor.getColumnIndex("VideoID")));
			cursor.moveToNext();
		}
		
		return VideoIDs;
		
	}
	/****** Slider Images *********/
	
	public boolean InsertSliderImages(String News){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("News", News);
		
		if(database.insert("News", null, values)>0)
			return true;
		else {
			return false;
		}
	}
	
	public ArrayList<String> getSliderImages() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor c=database.rawQuery("select * from SliderImages", null);
		c.moveToFirst();
		ArrayList<String>images=new ArrayList<String>();
		while (c.isAfterLast()==false) {
			images.add(c.getString(c.getColumnIndex("Images")));
			c.moveToNext();
		}
		
		return images;
	}
	
	/****** News *********/
	
	public boolean InsertDrawerItems(String image,String category){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("Images", image);
		values.put("category", category);
		
		if(database.insert("DrawerItems", null, values)>0)
			return true;
		else {
			return false;
		}
	}
	
	public ArrayList<NavDrawerItem> getDrawerItems() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor c=database.rawQuery("select * from DrawerItems", null);
		c.moveToFirst();
		ArrayList<NavDrawerItem>navigations=new ArrayList<NavDrawerItem>();
		while (c.isAfterLast()==false) {
			NavDrawerItem item=new NavDrawerItem();
			item.setIcon(c.getString(c.getColumnIndex("Images")));
			item.setTitle(c.getString(c.getColumnIndex("category")));
			navigations.add(item);
			c.moveToNext();
		}
		
		return navigations;
	}
	
	public ArrayList<String> getDrawerCategory() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor c=database.rawQuery("select * from DrawerItems", null);
		c.moveToFirst();
		ArrayList<String>DrawerItems=new ArrayList<String>();
		while (c.isAfterLast()==false) {
			DrawerItems.add(c.getString(c.getColumnIndex("category")));
			c.moveToNext();
		}
		
		return DrawerItems;
	}
	
	/****************** Service Table ******************/
	
	public boolean InsertServices(String category,String image,String Service){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put("category", category);
		values.put("Images", image);
		values.put("service", Service);
		
		if (database.insert("Services", null, values) > 0) {
			database.close();
			return true;
		}

		else {
			database.close();
			return false;
		}		
	}
	
	public ArrayList<ServiceItems> getServices(String category) {
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("select * from Services where category like '%"+category+"%'",null);
		cursor.moveToFirst();
		
		ArrayList<ServiceItems>services=new ArrayList<ServiceItems>();
		while (cursor.isAfterLast()==false) {
			ServiceItems serv=new ServiceItems();
			serv.setImage(cursor.getString(cursor.getColumnIndex("Images")));
			serv.setCategory(cursor.getString(cursor.getColumnIndex("service")));
			
			services.add(serv);
			
			cursor.moveToNext();
			
		}
		cursor.close();
		
		return services;
		
	}
	
	
	public ArrayList<String> getServiceNames(String category) {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from Services where category like '%" + category + "%'", null);
		cursor.moveToFirst();

		ArrayList<String> servicename = new ArrayList<String>();

		while (cursor.isAfterLast() == false) {

			servicename.add(cursor.getString(cursor.getColumnIndex("service")));

			cursor.moveToNext();

		}
		cursor.close();

		return servicename;

	}
	
	/******************Blogs Table ******************/
	
	public boolean InsertBlogs(String Name,String Blog,String date,String Region){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put("Name", Name);
		values.put("Blog", Blog);
		values.put("date", date);
		values.put("Region", Region);
		
		if (database.insert("Blogs", null, values) > 0) {
			database.close();
			return true;
		}

		else {
			database.close();
			return false;
		}		
	}
	public ArrayList<BlogItems> getBlogs() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("select * from Blogs",null);
		cursor.moveToFirst();
		
		ArrayList<BlogItems>blogs=new ArrayList<BlogItems>();
		while (cursor.isAfterLast()==false) {
			BlogItems blog=new BlogItems();
			blog.setName(cursor.getString(cursor.getColumnIndex("Name")));
			blog.setBlog(cursor.getString(cursor.getColumnIndex("Blog")));
			blog.setDate(cursor.getString(cursor.getColumnIndex("date")));
			blog.setRegion(cursor.getString(cursor.getColumnIndex("Region")));
			
			blogs.add(blog);
			
			cursor.moveToNext();
			
		}
		cursor.close();
		
		return blogs;
		
	}
	
	public ArrayList<String> getblogname() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from Blogs", null);
		cursor.moveToFirst();

		ArrayList<String> blognames = new ArrayList<String>();
		while (cursor.isAfterLast() == false) {

			blognames.add(cursor.getString(cursor.getColumnIndex("Name")));

			cursor.moveToNext();

		}
		cursor.close();

		return blognames;

	}
	
	/********** Property Table *************/
	public boolean InsertProperties(String PropertyType,String Image,String title,String location,String Mobile,String amount){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put("PropertyType", PropertyType);
		values.put("Image", Image);
		values.put("title", title);
		values.put("location", location);
		values.put("Mobile", Mobile);
		values.put("amount", amount);
		
		if (database.insert("Property", null, values) > 0) {
			database.close();
			return true;
		}

		else {
			database.close();
			return false;
		}		
	}
	
	public ArrayList<BuyItems> getProperty(String propertytype) {
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("select * from Property where PropertyType like '%"+propertytype+"%'",null);
		cursor.moveToFirst();
		
		ArrayList<BuyItems>properties=new ArrayList<BuyItems>();
		while (cursor.isAfterLast()==false) {
			BuyItems items=new BuyItems();
			items.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			items.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
			items.setImage(cursor.getString(cursor.getColumnIndex("Image")));
			items.setMobile(cursor.getString(cursor.getColumnIndex("Mobile")));
			items.setLocation(cursor.getString(cursor.getColumnIndex("location")));
			
			properties.add(items);
			
			cursor.moveToNext();
			
		}
		cursor.close();
		
		return properties;
		
	}
	
	public ArrayList<String> getPropertyname(String propertyType) {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from Property where PropertyType like '%"+propertyType+"%'", null);
		cursor.moveToFirst();

		ArrayList<String> blognames = new ArrayList<String>();
		while (cursor.isAfterLast() == false) {

			blognames.add(cursor.getString(cursor.getColumnIndex("title")));

			cursor.moveToNext();

		}
		cursor.close();

		return blognames;

	}
	
	/************ small Slider *************/
	
	
	public boolean InsertSmallSliderImages(String News){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("Images", News);
		
		if(database.insert("SmallSliderImages", null, values)>0)
			return true;
		else {
			return false;
		}
	}
	
	public ArrayList<String> getSmallSliderImages() {
		SQLiteDatabase database=getReadableDatabase();
		Cursor c=database.rawQuery("select * from SmallSliderImages", null);
		c.moveToFirst();
		ArrayList<String>images=new ArrayList<String>();
		while (c.isAfterLast()==false) {
			images.add(c.getString(c.getColumnIndex("Images")));
			c.moveToNext();
		}
		
		return images;
	}
	
	
	/********** ContactAPI Table *************/
	public boolean InsertContacts(String Category,String Name,String Image,String Adress,String contactnumber,String website){
		SQLiteDatabase database=getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put("Category", Category);
		values.put("Image", Image);
		values.put("Name", Name);
		values.put("Adress", Adress);
		values.put("contactnumber", contactnumber);
		values.put("website", website);
		
		if (database.insert("ContactAPI", null, values) > 0) {
			database.close();
			return true;
		}

		else {
			database.close();
			return false;
		}		
	}
	
	public ArrayList<ContactsItems> getContacts(String Category) {
		SQLiteDatabase database=getReadableDatabase();
		Cursor cursor=database.rawQuery("select * from ContactAPI where Category like '%"+Category+"%'",null);
		cursor.moveToFirst();
		
		ArrayList<ContactsItems>Contacts=new ArrayList<ContactsItems>();
		while (cursor.isAfterLast()==false) {
			ContactsItems items=new ContactsItems();
			items.setName(cursor.getString(cursor.getColumnIndex("Name")));
			items.setImage(cursor.getString(cursor.getColumnIndex("Image")));
			items.setAdress(cursor.getString(cursor.getColumnIndex("Adress")));
			items.setContactnumber(cursor.getString(cursor.getColumnIndex("contactnumber")));
			items.setWebsite(cursor.getString(cursor.getColumnIndex("website")));
			
			Contacts.add(items);
			
			cursor.moveToNext();
			
		}
		cursor.close();
		
		return Contacts;
		
	}
	
	
	public ArrayList<String> getContactNumber(String Category) {
		SQLiteDatabase database=getReadableDatabase();
		Cursor c=database.rawQuery("select * from ContactAPI where Category like '%"+Category+"%'",null);
		c.moveToFirst();
		ArrayList<String>images=new ArrayList<String>();
		while (c.isAfterLast()==false) {
			images.add(c.getString(c.getColumnIndex("contactnumber")));
			c.moveToNext();
		}
		
		return images;
	}
	
	
}
