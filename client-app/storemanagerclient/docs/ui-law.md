# UI Law: VisionOS-Like Cinematic Glass Design System

This is the official visual direction for the JavaFX Store Manager application.
Future UI work must follow this system consistently.

## Target Aesthetic

The application should feel like a visionOS-inspired cinematic control system:

- Apple-like discipline
- Atmospheric cold environment
- Translucent floating glass surfaces
- Calm minimal objects
- Restrained warm energy signals
- Premium industrial control-panel clarity

This is not a cyberpunk UI, gamer RGB UI, material dashboard, or flashy admin panel.

## Core Philosophy

Background equals atmosphere.

The background owns gradients, lighting, color energy, cinematic depth, and environmental feeling.
Visual richness belongs mostly to the environment.

Objects are quiet.

Buttons, cards, panels, and sidebar items must stay calm, simple, translucent, soft, and readable.
Objects should never visually fight the environment.

## Color Law

Use an 85 percent cold, 15 percent warm balance.

Cold colors dominate:

- Background
- Environment
- Depth
- Atmosphere
- Spatial layering

Warm colors are reserved for:

- Signals
- Alerts
- Active focus
- Important status
- Critical numbers
- Notifications
- Live indicators

Warm color must be concentrated, intentionally rare, and never dominant.

## Background Rules

Backgrounds must use atmospheric gradients, subtle cold lighting, ambient depth, and soft environmental variation.

Preferred cold tones:

```text
#dfeafe
#edf4ff
#f7fbff
```

Allowed:

- Subtle warm ambient streaks
- Cinematic lighting zones
- Soft blur atmosphere

Forbidden:

- Flat dead white backgrounds
- Rainbow gradients
- Colorful noise

## Component Rules

Buttons must remain simple, mostly flat, translucent, and minimally styled.

Allowed:

- Subtle translucency
- Soft glass blur
- Light borders
- Luminance-based focus

Forbidden:

- Strong gradient buttons
- Neon buttons
- Fake glossy 3D
- Heavy shadows

Cards and panels should float softly with translucency and blur.
Depth should come from transparency, blur, and luminance separation, not shadow stacking.

Preferred surface opacity:

```text
rgba(255,255,255,0.12)
rgba(255,255,255,0.18)
rgba(255,255,255,0.28)
```

## Sidebar Rules

The sidebar should feel floating, spatial, translucent, and integrated into the environment.
It must not read as a hard admin navigation rail.

Sidebar items use soft capsules, subtle active states, minimal decoration, and cold-dominant color.
The active item should rely mostly on luminance change and soft cold tint.
A tiny warm edge signal is allowed only when it supports status or focus.

## Geometry, Blur, And Shadow

Use large rounded geometry:

```text
20px
24px
28px
32px
```

Blur and translucency are core design elements for spatial separation.

Shadows must stay soft, subtle, diffused, and atmospheric.
Avoid dark hard shadows and material-style elevation stacking.

## Typography And Spacing

Hierarchy should come from spacing, scale, weight, and luminance.

Prefer:

- Medium visual weight
- Calm spacing
- Clean alignment
- Large breathing room

Avoid:

- Excessive bold text
- Noisy hierarchy
- Cramped layouts
- Color-driven hierarchy

## Implementation Rules

All reusable styling belongs in:

```text
src/main/resources/theme/app-theme.css
```

Avoid duplicated inline styles, one-off visual experiments, and inconsistent component styling.
All future modules must reuse the same spacing, glass, component, and atmospheric rules.
