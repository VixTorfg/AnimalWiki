package com.example.animalwiki.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animalwiki.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapFragment extends Fragment {

    private Button zoomInButton;
    private Button zoomOutButton;

    private final String API_URL = "https://api.api-ninjas.com/v1/animals?name=";
    private final String API_KEY = "4w6KE4S22ypBI3Ibz7kJ/w==bzsWUNM8WpVhCdMR";

    private MapView mapView;
    private IMapController mapController;
    private Map<String, GeoPoint> locationMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Configuración de osmdroid
        Context context = requireContext().getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(context, android.preference.PreferenceManager.getDefaultSharedPreferences(context));

        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Fuente de los tiles (mapas)
        mapView.setMultiTouchControls(true); // Permitir zoom con dos dedos

        mapController = mapView.getController();
        mapController.setZoom(3.0); // Zoom inicial
        mapView.setMinZoomLevel(3.0); // Zoom mínimo
        mapView.setMaxZoomLevel(18.0); // Zoom máximo
        GeoPoint startPoint = new GeoPoint(0.0, 0.0); // Punto inicial (centro del mapa)
        mapController.setCenter(startPoint);

        // Obtener referencias a los botones
        zoomInButton = view.findViewById(R.id.zoomInButton);
        zoomOutButton = view.findViewById(R.id.zoomOutButton);

        // Añadir listeners a los botones
        zoomInButton.setOnClickListener(v -> mapController.zoomIn());
        zoomOutButton.setOnClickListener(v -> mapController.zoomOut());

        // Inicializar el mapa de ubicaciones
        initializeLocationMap();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        // Recargar los favoritos cada vez que el fragmento se vuelve visible
        loadFavoriteAnimalLocations();
    }

    private void initializeLocationMap() {
        locationMap = new HashMap<>();
        locationMap.put("africa", new GeoPoint(10.0, 20.0));
        locationMap.put("asia", new GeoPoint(30.0, 100.0));
        locationMap.put("europe", new GeoPoint(50.0, 10.0));
        locationMap.put("northamerica", new GeoPoint(40.0, -100.0));
        locationMap.put("southamerica", new GeoPoint(-20.0, -60.0));
        locationMap.put("centralamerica", new GeoPoint(12.5, -85.0));
        locationMap.put("ocean", new GeoPoint(0.0, 0.0));
        locationMap.put("australia", new GeoPoint(-25.0, 135.0));
        locationMap.put("oceania", new GeoPoint(-10.0, 140.0)); // Coordenadas aproximadas
        locationMap.put("eurasia", new GeoPoint(50.0, 80.0)); // Coordenadas aproximadas
    }
    private void loadFavoriteAnimalLocations() {
        // Limpiar los marcadores existentes antes de añadir nuevos
        mapView.getOverlays().clear();
        new Thread(() -> {
            try {
                // Obtener la lista de animales favoritos desde SharedPreferences
                Map<String, ?> favorites = requireContext()
                        .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                        .getAll();

                Set<String> favoriteAnimalNames = favorites.keySet();

                for (String animalName : favoriteAnimalNames) {
                    URL url = new URL(API_URL + animalName);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("accept", "application/json");
                    connection.setRequestProperty("X-Api-Key", API_KEY);

                    if (connection.getResponseCode() == 200) {
                        InputStream responseStream = connection.getInputStream();
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(responseStream);

                        if (root.isArray() && root.size() > 0) {
                            JsonNode animalNode = root.get(0);
                            JsonNode locationsNode = animalNode.path("locations");

                            if (locationsNode.isArray() && locationsNode.size() > 0) {
                                for (JsonNode location : locationsNode) {
                                    String locationName = location.asText().toLowerCase().replace("-", "").replace(" ", "");
                                    GeoPoint geoPoint = getGeoPointFromLocationName(locationName);
                                    if (geoPoint != null) {
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            // Añadir un marcador al mapa
                                            Marker marker = new Marker(mapView);
                                            marker.setPosition(geoPoint);
                                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                            marker.setTitle(animalName);
                                            marker.setSnippet(location.asText());
                                            mapView.getOverlays().add(marker);
                                        });
                                    }
                                }
                            }
                        }
                    }
                    connection.disconnect();
                }
            } catch (Exception e) {
                Log.e("MapFragment", "Error loading favorite animal locations", e);
            }
        }).start();
    }
    private GeoPoint getGeoPointFromLocationName(String locationName) {
        GeoPoint geoPoint = locationMap.get(locationName);
        return geoPoint;
    }
    private BoundingBox getBoundingBox(List<Overlay> overlays) {
        if (overlays.isEmpty()) {
            return null;
        }
        double north = Double.NEGATIVE_INFINITY;
        double south = Double.POSITIVE_INFINITY;
        double east = Double.NEGATIVE_INFINITY;
        double west = Double.POSITIVE_INFINITY;

        for (Overlay overlay : overlays) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                GeoPoint position = marker.getPosition();
                north = Math.max(north, position.getLatitude());
                south = Math.min(south, position.getLatitude());
                east = Math.max(east, position.getLongitude());
                west = Math.min(west, position.getLongitude());
            }
        }
        return new BoundingBox(north, east, south, west);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDetach();
    }
}
