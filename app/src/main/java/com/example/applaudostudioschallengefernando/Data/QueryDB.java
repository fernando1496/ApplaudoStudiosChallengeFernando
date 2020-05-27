package com.example.applaudostudioschallengefernando.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


public class QueryDB {
	//table names
	private static final String sTableAnime = "Anime";
	private static final String sTableCategories = "Categories";
	private Context cContext;
	private SQLiteDatabase dbDatabBase;
	private ManagerDB dbHelper;

	public QueryDB(Context context) {
		this.cContext = context;
	}
	public QueryDB open() throws SQLException {
		dbHelper = new ManagerDB(cContext);
		dbDatabBase = dbHelper.getWritableDatabase();
		return this;
	}
	public void  BeginTransaction()  {
		dbDatabBase.beginTransaction();
	}
	public void  CommitTransaction()  {
		dbDatabBase.setTransactionSuccessful();
		dbDatabBase.endTransaction();
	}
	public void  EndTransaction()  {

		dbDatabBase.endTransaction();
	}
	public void close() {
		dbHelper.close();
	}

	//Querys to SqlLite
	public boolean DeleteAnime() {
		boolean transact = false;
		if (dbDatabBase != null && dbDatabBase.isOpen()) {
			transact = dbDatabBase.delete(sTableAnime, null, null) > 0;
		}
		return (transact);
	}

	public boolean DeleteCategories() {
		boolean transact = false;
		if (dbDatabBase != null && dbDatabBase.isOpen()) {
			transact = dbDatabBase.delete(sTableCategories, null, null) > 0;
		}
		return (transact);
	}

	//Insert Animes
	public boolean InsertAnime(int iCategoryId, int iAnimeId, String sTitle, String sImageUrl, String sAnimeSlug, String sType, String sNumberE, String sStartYear,
							   String sEndYear, String sCanonTitle, String sAverageRating, String sAgeRating, String sEpisodeDuration, String sAiringStatus, String Synopsys, String sYoutubeId)  {
		String sNewSqlDate = "";

		String sqlQuery = "insert into " + sTableAnime + " (CategoryId, AnimeId, AnimeTitle, ImageUrl, AnimeSlug, Type , NumberE , StartYear , EndYear , CanonTitle ,AverageRating , AgeRating , " +
				"EpisodeDuration ,AiringStatus , Synopsys , YouTubeId ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


		SQLiteStatement sqlStatement = dbDatabBase.compileStatement(sqlQuery);
		sqlStatement.bindDouble(1, iCategoryId);
		sqlStatement.bindDouble(2, iAnimeId);
		sqlStatement.bindString(3, sTitle);
		sqlStatement.bindString(4, sImageUrl);
		sqlStatement.bindString(5, sAnimeSlug);
		sqlStatement.bindString(6, sType);
		sqlStatement.bindString(7, sNumberE);
		sqlStatement.bindString(8, sStartYear);
		sqlStatement.bindString(9, sEndYear);
		sqlStatement.bindString(10, sCanonTitle);
		sqlStatement.bindString(11, sAverageRating);
		sqlStatement.bindString(12, sAgeRating);
		sqlStatement.bindString(13, sEpisodeDuration);
		sqlStatement.bindString(14, sAiringStatus);
		sqlStatement.bindString(15, Synopsys);
		sqlStatement.bindString(16, sYoutubeId);
		Long lResult = sqlStatement.executeInsert();
		sqlStatement.clearBindings();

		return (lResult > 0);
	}

	//Insert Categories
	public boolean InsertCategory(int iCategoryId, String sCategoryTitle) {
		String sNewSqlDate = "";

		String sqlQuery = "insert into " + sTableCategories + " (CategoryId,CategoryTitle) values (?, ?)";


		SQLiteStatement sqlStatement = dbDatabBase.compileStatement(sqlQuery);
		sqlStatement.bindDouble(1, iCategoryId);
		sqlStatement.bindString(2, sCategoryTitle);
		Long lResult = sqlStatement.executeInsert();
		sqlStatement.clearBindings();

		return (lResult > 0);
	}

	public Cursor GetAnimesByCategories(int iCategoryId) {
		String sParam[] = new String[] { Integer.toString(iCategoryId) };
		return dbDatabBase.rawQuery("select AnimeId,CanonTitle, ImageUrl from Anime where CategoryId = ?" , sParam);
	}

	public Cursor GetCategories() {
		return dbDatabBase.rawQuery("select CategoryId,CategoryTitle from Categories" , null);
	}

	public Cursor GetAnimeById(int iAnimeId) {
		String sParam[] = new String[] { Integer.toString(iAnimeId) };
		return dbDatabBase.rawQuery("select CategoryId, AnimeId, AnimeTitle, ImageUrl, AnimeSlug, Type , NumberE , StartYear , EndYear , CanonTitle ,AverageRating , AgeRating , EpisodeDuration ,AiringStatus , Synopsys , YouTubeId from Anime where AnimeId = ?" , sParam);
	}

	public Cursor GetCategoryById(int iCategoryId) {
		String sParam[] = new String[] { Integer.toString(iCategoryId) };
		return dbDatabBase.rawQuery("select CategoryId,CategoryTitle from Categories where CategoryId = ?" , sParam);
	}

}
