package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

public interface ISliderButton
{
	void handlerSliderAction(int sliderId, float sliderValue);

	String getSliderName(int sliderId, float sliderValue);
}