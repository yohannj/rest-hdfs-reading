package input;

import java.util.Collections;
import java.util.Set;

import utils.EFileFormat;

public class LoadDTO {

	private Set<String> aggregationColumns;
	private String path;
	private String viewName;
	private EFileFormat format;

	public LoadDTO() {
		this.aggregationColumns = Collections.emptySet();
	}

	public Set<String> getAggregationColumns() {
		return aggregationColumns;
	}

	public void setAggregationColumns(Set<String> aggregationColumns) {
		this.aggregationColumns = aggregationColumns;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public EFileFormat getFormat() {
		return format;
	}

	public void setFormat(EFileFormat format) {
		this.format = format;
	}

}
