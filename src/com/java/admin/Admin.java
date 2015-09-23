//-------------------------------------------------------------------------------------------------------------------------------------------------
package com.java.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Scanner;

import com.java.record.Record;

//-------------------------------------------------------------------------------------------------------------------------------------------------

/**
 * Engineered and developed by Jhonny Trejos Barrios.
 * Technology: Java.
 * Version: Java Development Kit 1.8.0_31, Standard Edition.
 * Development Environment: Sublime Text 3.
 * ------------------------------------------------------------------------------------------------------------------------------------------------
 * ------------------------------------------------------------------------------------------------------------------------------------------------
 * Additional Info.
 *
 * Source Code Target:
 *
 *			[ MANAGE RECORDS. CREATE, DELETE AND MAKE BACK-UPS ].
 *
 * Licenses: GNU GPL v3.0, Eclipse Public License 1.0, personal not for commercial purposes.
 * Developer Contact: jtrejosb@live.com || jtrejosb@gmail.com || jtrejosb@icloud.com
 * Mobile: --.
 */

//-------------------------------------------------------------------------------------------------------------------------------------------------

public class Admin
{
	FileInputStream FIS;
	ObjectInputStream OIS;
	FileOutputStream FOS;
	ObjectOutputStream OOS;
	File F = new File( "/YOUR_FILEPATH/src/com/java/record/Records.dat" );
	File backUp = new File( "/YOUR_FILEPATH/src/com/java/record/backUp/Records(secured).dat" );
	ArrayList < Record > records = new ArrayList <>();
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	public static void main( String[] args )
	{
	    // new Admin().newRecord( "0001", "Record 0001 Name", "Record 0001 Age", "Record 0001 City", "Record 0001 Old" );
		// new Admin().newRecord( "0002", "Record 0002 Name", "Record 0002 Age", "Record 0002 City", "Record 0002 Old" );
		// new Admin().newRecord( "0003", "Unknown Name", "Unknown Age", "Unknown City", "Unknown Oldest" );

		// System.out.print( "Select record in position: " );
		// new Admin().selectRecord( new Scanner( System.in ).nextInt() );

		new Admin().showAllRecords();

		// System.out.print( "Select record position to delete: " );
		// new Admin().deleteRecord( new Scanner( System.in ).nextInt() );

		// new Admin().deleteDuplicateRecords();

		// new Admin().deleteAllRecords();

		/**  Type 'view' to show the content of the actual backup or 'recover' to import all records from your latest backup.  */
		// new Admin().backUps( "view" );
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	public void selectRecord( int value )
	{
		loadData();

		if( ( value > 0 ) && ( value <= records.size() ) )
		{
			System.out.println( records.get( value - 1 ).getRecordInfo() );

			System.err.println( "Showing record " + value + " of " + records.size() );
		}
		else
		{
			System.err.println( "Error01.\nInvalid range, to select a record retype a value between 1 and " + records.size() );
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	public void showAllRecords()
	{
		loadData();

		for( Record R : records )
		{
			System.out.println( R.getRecordInfo() );
		}

		System.err.println( "Showing " + records.size() + " records." );
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	public void deleteRecord( int value )
	{
		loadData();

		if( ( value > 0 ) && ( value <= records.size() ) )
		{
			try
			{
				System.out.println( records.get( value - 1 ).getRecordInfo() );

				FOS = new FileOutputStream( F );
				OOS = new ObjectOutputStream( FOS );

				records.remove( value - 1 );

				OOS.writeObject( records );

				OOS.close();
				FOS.close();

				System.err.println( "This record has been deleted." );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println( "Error02.\nInvalid range, to delete a record retype a value between 1 and " + records.size() );
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	public void deleteDuplicateRecords()
	{
		loadData();

		int duplicated = 0;

		for( int i = 0; i < records.size() - 1; i++ )
		{
			for( int j = i + 1; j < records.size(); j++ )
			{
				if( records.get( i ).getRecordInfo().equals( records.get( j ).getRecordInfo() ) )
				{
					records.remove( j );
					duplicated ++;
					j --;
				}
			}
		}

		if( duplicated > 0 )
		{
			try
			{
				FOS = new FileOutputStream( F );
				OOS = new ObjectOutputStream( FOS );

				OOS.writeObject( records );

				OOS.close();
				FOS.close();

				System.err.println( duplicated + " duplicated records has been deleted." );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println( "No duplicated records has been found." );
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	public void deleteAllRecords()
	{
		loadData();

		try
		{
			int total = records.size();

			FOS = new FileOutputStream( F );
			OOS = new ObjectOutputStream( FOS );

			records.removeAll( records );

			OOS.writeObject( records );

			OOS.close();
			FOS.close();

			System.err.println( "All records deleted.\nTotal: " + total );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings( "unchecked" )
	public void backUps( String mode )
	{
		if( backUp.exists() )
		{
			try
			{
				FIS = new FileInputStream( backUp );
				OIS = new ObjectInputStream( FIS );

				records = ( ArrayList < Record > ) OIS.readObject();

				OIS.close();
				FIS.close();

				boolean flag = false;

				for( int i = 0; i < records.size() - 1; i++ )
				{
					for( int j = i + 1; j < records.size(); j++ )
					{
						if( records.get( i ).getRecordInfo().equals( records.get( j ).getRecordInfo() ) )
						{
							records.remove( j );
							flag = true;
							j --;
						}
					}
				}

				if( flag )
				{
					FOS = new FileOutputStream( backUp );
					OOS = new ObjectOutputStream( FOS );

					OOS.writeObject( records );

					OOS.close();
					FOS.close();
				}

				for( Record R : records )
				{
					System.out.println( R.getRecordInfo() );
				}

				System.err.println( "Successfully loaded!\nContent of this back-up: " + records.size() + " record(s)." );

				if( mode.equals( "recover" ) )
				{
					FOS = new FileOutputStream( F );
					OOS = new ObjectOutputStream( FOS );

					OOS.writeObject( records );

					OOS.close();
					FOS.close();

					System.err.println( "Imported records.\nTotal recovered: " + records.size() + " record(s)." );
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println( "No backUps are available now." );
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings( { "unchecked", "ResultOfMethodCallIgnored" } )
	public void newRecord( String code, String name, String age, String city, String oldest )
	{
		try
		{
			if( ! backUp.exists() )
			{
				new File( backUp.getParent() ).mkdirs();
			}

			if( F.exists() )
			{
				FIS = new FileInputStream( F );
				OIS = new ObjectInputStream( FIS );

				records = ( ArrayList < Record > ) OIS.readObject();

				OIS.close();
				FIS.close();
			}

			FOS = new FileOutputStream( F );
			OOS = new ObjectOutputStream( FOS );

			records.add( new Record( code, name, age, city, oldest ) );

			OOS.writeObject( records );

			OOS.close();
			FOS.close();

			/**  __________  SAVING BACKUP...  ________________________________________________________________________________________________  */

			records.removeAll( records );

			if( backUp.exists() )
			{
				FIS = new FileInputStream( backUp );
				OIS = new ObjectInputStream( FIS );

				records = ( ArrayList < Record > ) OIS.readObject();

				OIS.close();
				FIS.close();
			}

			FOS = new FileOutputStream( backUp );
			OOS = new ObjectOutputStream( FOS );

			records.add( new Record( code, name, age, city, oldest ) );

			OOS.writeObject( records );

			OOS.close();
			FOS.close();
			/**  ______________________________________________________________________________________________________________________________  */

			System.err.println( "Successfully saved in: " + F.getParent() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings( { "unchecked", "ResultOfMethodCallIgnored" } )
	public void loadData()
	{
		try
		{
			if( ! backUp.exists() )
			{
				new File( backUp.getParent() ).mkdirs();
			}

			if( F.exists() )
			{
				FIS = new FileInputStream( F );
				OIS = new ObjectInputStream( FIS );

				records = ( ArrayList < Record > ) OIS.readObject();

				OIS.close();
				FIS.close();

				if( records.isEmpty() )
				{
					System.err.println( "Master file is empty." );
					System.exit( 0 );
				}
			}
			else
			{
				System.err.println( "Master file doesn't exists." );
				System.exit( 0 );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------------------------------------------------------------------
