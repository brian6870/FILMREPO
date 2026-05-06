# Filmpire CloudStream Extension

Watch free movies and TV shows on CloudStream via TMDB API with VidSrc streaming sources.

## Features

- Browse trending movies and TV shows
- Search across movies and TV series
- Episode lists for TV shows with season support
- Multiple streaming sources (VidSrc, ScreenFetch)

## Public Installation

1. Open **CloudStream** on your Android TV / device
2. Go to **Settings** → **Extensions** → **Add Repository**
3. Enter this URL:

https://raw.githubusercontent.com/brian6870/FILMREPO/main/repo.json
text
CopyDownload

4. Tap **Add** → The Filmpire extension will appear under available extensions
5. Install and start streaming

## Manual Installation

Download the latest `.cs3` file from the repository and sideload via:
- **ADB:** `adb install FilmpireExtension.cs3`
- **File Manager:** Open the file on your device and install

## Build from Source

```bash
git clone git@github.com:brian6870/FILMREPO.git
cd FILMREPO
export ANDROID_HOME=/path/to/android-sdk
./gradlew :FilmpireExtension:make
# Output: FilmpireExtension/build/FilmpireExtension.cs3
LicenseMIT — see [LICENSE](LIC
