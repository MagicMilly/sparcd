package model.analysis.textFormatters;

import java.util.List;

import model.analysis.DataAnalysis;
import model.analysis.PredicateBuilder;
import model.analysis.SanimalAnalysisUtils;
import model.image.ImageEntry;
import model.location.Location;
import model.location.UTMCoord;
import model.species.Species;

/**
 * The text formatter for species with location/utm/latlng coordinates
 * 
 * @author David Slovikosky
 */
public class SpeciesLocCoordFormatter extends TextFormatter
{
	public SpeciesLocCoordFormatter(List<ImageEntry> images, DataAnalysis analysis)
	{
		super(images, analysis);
	}

	/**
	 * <p>
	 * Dr. Jim Sanderson's description:
	 * <p>
	 * For each species a list of locations where the species was recorded, and the UTM and elevation of the location.
	 * 
	 * @return Returns a string representing the data in a clean form
	 */
	public String printSpeciesByLocWithUTM()
	{
		String toReturn = "";

		toReturn = toReturn + "SPECIES BY LOCATION WITH UTM AND ELEVATION\n";
		for (Species species : analysis.getAllImageSpecies())
		{
			List<ImageEntry> withSpecies = new PredicateBuilder().speciesOnly(species).query(images);
			toReturn = toReturn + species.getName() + "\n";
			toReturn = toReturn + "Location                        UTMe-w   UTMn-s    Elevation   Lat        Long\n";
			for (Location location : analysis.locationsForImageList(withSpecies))
			{
				UTMCoord coord = SanimalAnalysisUtils.Deg2UTM(location.getLat(), location.getLng());
				toReturn = toReturn + String.format("%-28s  %8d  %8d  %7.0f      %8.6f  %8.6f\n", location.getName(), Math.round(coord.getEasting()), Math.round(coord.getNorthing()), location.getElevation(), location.getLat(), location.getLng());
			}
			toReturn = toReturn + "\n";
		}

		return toReturn;
	}
}
