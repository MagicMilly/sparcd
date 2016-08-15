package model.analysis.textFormatters;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.analysis.DataAnalysis;
import model.analysis.PredicateBuilder;
import model.image.ImageEntry;
import model.location.Location;
import model.species.Species;

/**
 * The text formatter for richness calculations
 * 
 * @author David Slovikosky
 */
public class RichnessFormatter extends TextFormatter
{
	public RichnessFormatter(List<ImageEntry> images, DataAnalysis analysis)
	{
		super(images, analysis);
	}

	/**
	 * <p>
	 * Dr. Jim Sanderson's description:
	 * <p>
	 * A table of locations vs. species showing the number of pictures of each species recorded at the location. The last column shows the number of
	 * species recorded at the location (Rich), and the last row shows total number of loations a species was recorded at (Richness)
	 * 
	 * @return Returns a string representing the data in a clean form
	 */
	public String printLocationSpeciesRichness()
	{
		String toReturn = "";

		toReturn = toReturn + "LOCATIONS BY SPECIES AND LOCATION AND SPECIES RICHNESS\n";
		toReturn = toReturn + "  One record of each species per location per PERIOD\n";
		toReturn = toReturn + "Location                          ";
		for (Species species : analysis.getAllImageSpecies())
			toReturn = toReturn + String.format("%-6s ", StringUtils.left(species.getName(), 6));
		toReturn = toReturn + "Rich\n";

		for (Location location : analysis.getAllImageLocations())
		{
			toReturn = toReturn + String.format("%-28s       ", location.getName());
			List<ImageEntry> imagesAtLoc = new PredicateBuilder().locationOnly(location).query(analysis.getImagesSortedByDate());

			Integer horizontalRichness = 0;
			for (Species species : analysis.getAllImageSpecies())
			{
				List<ImageEntry> imagesAtLocWithSpecies = new PredicateBuilder().speciesOnly(species).query(imagesAtLoc);
				Integer period = analysis.periodForImageList(imagesAtLocWithSpecies);
				horizontalRichness = horizontalRichness + (period == 0 ? 0 : 1);
				toReturn = toReturn + String.format("%5d  ", period);
			}
			toReturn = toReturn + String.format("%5d  ", horizontalRichness);

			toReturn = toReturn + "\n";
		}
		toReturn = toReturn + "Richness                           ";

		for (Species species : analysis.getAllImageSpecies())
		{
			Integer richness = 0;
			List<ImageEntry> imagesWithSpecies = new PredicateBuilder().speciesOnly(species).query(images);
			for (Location location : analysis.getAllImageLocations())
			{
				List<ImageEntry> imagesWithSpeciesAtLoc = new PredicateBuilder().locationOnly(location).query(imagesWithSpecies);
				richness = richness + (imagesWithSpeciesAtLoc.size() == 0 ? 0 : 1);
			}
			toReturn = toReturn + String.format("%5d  ", richness);
		}
		toReturn = toReturn + "\n";

		toReturn = toReturn + "\n";

		return toReturn;
	}
}
