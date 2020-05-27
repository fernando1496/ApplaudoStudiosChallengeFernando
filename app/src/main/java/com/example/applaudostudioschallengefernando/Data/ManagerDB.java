package com.example.applaudostudioschallengefernando.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ManagerDB extends SQLiteOpenHelper {
	private static final String TAG = "Kitsu";

	//Anime table
	private static final String sCreateTableAnime = "create table if not exists Anime(CategoryId integer, AnimeId integer, AnimeTitle text, " +
			"ImageUrl text, AnimeSlug text, Type text, NumberE text, StartYear text, EndYear text, CanonTitle text,AverageRating text, AgeRating text, EpisodeDuration text," +
			"AiringStatus text, Synopsys text, YouTubeId text);";
	private static final String sDropTableAnime = "drop table if exists Anime;";

	//Anime table
	private static final String sCreateTableCategories = "create table if not exists Categories(CategoryId integer, CategoryTitle text);";
	private static final String sDropTableCategories = "drop table if exists Categories;";

	// database name and ver.
	private static final String databaseName = "Kitsu";
	private static final int databaseVersion = 2;

	public ManagerDB(Context CtxContext) {
		super(CtxContext, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase dbDataBase) {
		try {

			dbDataBase.execSQL(sCreateTableAnime);
			dbDataBase.execSQL(sCreateTableCategories);

		} catch (Exception e) {
			Log.i(TAG, "Open: " + e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase dbDataBase, int iPreviousVersion,
                          int iNewVersion) {
		if (iNewVersion > iPreviousVersion) {
			try {
				//On version increase, drop and create the table again
				dbDataBase.execSQL(sDropTableAnime);
				dbDataBase.execSQL(sDropTableCategories);
				onCreate(dbDataBase);
			} catch (Exception e) {
				Log.i(TAG, "DbError:" + e);
			}

		}
	}
}
