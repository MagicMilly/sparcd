/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageImporterData
{
	private ImageDirectory head;

	public void readAndAddImages(File imageOrLocation, boolean recursive)
	{
		if (!imageOrLocation.isDirectory())
		{
			this.head = new ImageDirectory(imageOrLocation.getParentFile());
			this.head.addImage(new ImageEntry(imageOrLocation));
		}
		else
		{
			this.head = new ImageDirectory(imageOrLocation);
			this.createDirectoryAndImageTree(this.head, recursive);
		}
	}

	public ImageDirectory getHeadDirectory()
	{
		return this.head;
	}

	private void createDirectoryAndImageTree(ImageDirectory current, boolean recursive)
	{
		// Get all files in the directory
		File[] subfiles = current.getDirectory().listFiles();
		for (File file : subfiles)
		{
			// Add all image files to the directory
			if (fileIsImage(file))
			{
				current.addImage(new ImageEntry(file));
			}
			// Add all subdirectories to the directory
			else if (recursive && file.isDirectory())
			{
				ImageDirectory subDirectory = new ImageDirectory(file);
				current.addSubDirectory(subDirectory);
				this.createDirectoryAndImageTree(subDirectory, recursive);
			}
		}
	}

	private boolean fileIsImage(File file)
	{
		String result = null;
		try
		{
			result = Files.probeContentType(file.toPath());
		}
		catch (IOException e)
		{
		}
		if (result == null || !result.startsWith("image"))
			return false;
		return true;
	}
}