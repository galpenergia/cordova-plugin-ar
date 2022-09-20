package com.galp.plugins.ar;

import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;

public class CustomArActivity extends AppCompatActivity {

    private Renderable renderable;
    private ArFragment arFragment;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getResourceIdByName("activity_custom_ar", "layout"));
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(this.getResourceIdByName("ux_fragment", "id"));

        String resourcePath = getIntent().getStringExtra("RESOURCE_PATH");
        if(resourcePath.isEmpty()) {
            finish();
        }
        setUpModel(resourcePath);
        setUpPlane();
    }

    private void setUpModel(String resourcePath) {
        WeakReference<CustomArActivity> weakActivity = new WeakReference<>(this);

        ModelRenderable.builder()
                .setSource(
                        this,
                        Uri.parse(resourcePath))
                .setIsFilamentGltf(true)
                .setRegistryId(resourcePath)
                .build()
                .thenAccept(
                        modelRenderable -> {
                            CustomArActivity activity = weakActivity.get();
                            if (activity != null) {
                                activity.renderable = modelRenderable;
                            }
                        })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load Tiger renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    private void setUpPlane(){


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (renderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable model and add it to the anchor.
                    TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setParent(anchorNode);
                    model.setRenderable(renderable);
                    model.select();
        });
    }

    private int getResourceIdByName(String name, String defType) {
        return this.getResources().getIdentifier(name, defType, this.getPackageName());
    }
}