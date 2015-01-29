package de.livinglab.dashboard.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;;

@XmlRootElement(name = "LogisticsServiceProvider")
@XmlAccessorType (XmlAccessType.FIELD)
public class LogisticsServiceProvider {

	@XmlElement(name = "LogisticServiceProvider")
	private List<LogisticServiceProvider> logisticsServiceProvider = null;

	public List<LogisticServiceProvider> getLogisticsServiceProvider() {
		return logisticsServiceProvider;
	}

	public void setLogisticServiceProviders(
			List<LogisticServiceProvider> logisticsServiceProvider) {
		this.logisticsServiceProvider = logisticsServiceProvider;
	}
}
