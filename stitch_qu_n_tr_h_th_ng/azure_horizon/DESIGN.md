# Design System Specification: High-End Editorial E-Commerce

## 1. Overview & Creative North Star: "The Digital Atelier"
This design system rejects the "cookie-cutter" layout of generic e-commerce platforms. Our Creative North Star is **The Digital Atelier**—a space that feels curated, architectural, and premium. We move beyond standard grids by using intentional white space, tonal depth, and high-contrast typography to create an editorial experience that feels more like a high-end lookbook than a database.

**Key Principles:**
*   **Asymmetric Breathing Room:** Use generous, staggered padding (Scale `16` and `24`) to break the rigid vertical flow.
*   **Layered Authority:** Trust is built through sophisticated layering (Glassmorphism and Tonal Shifting) rather than heavy borders or aggressive shadows.
*   **Editorial Scale:** We use dramatic shifts between `display-lg` and `label-sm` to create a clear, authoritative information hierarchy.

---

## 2. Color & Surface Philosophy

### The "No-Line" Rule
**Prohibit 1px solid borders for sectioning.** To define boundaries, use background color shifts. 
*   Place a `surface_container_low` section directly against a `surface` background. 
*   For product grids, let the white space and the `surface_container_highest` image containers create the structure.

### Surface Hierarchy & Nesting
Treat the UI as a physical stack of materials. 
1.  **Base:** `surface` (#f7f9fb)
2.  **Sectioning:** `surface_container_low` (#f2f4f6)
3.  **Interactive Elements:** `surface_container_lowest` (#ffffff)
4.  **Admin Sidebar (High Contrast):** `inverse_surface` (#2d3133) with `on_surface_variant` text.

### The "Glass & Gradient" Rule
To inject "soul" into the professional blue:
*   **Hero Areas:** Use a subtle linear gradient from `primary` (#0040a1) to `primary_container` (#0056d2) at a 135-degree angle.
*   **Floating Navigation:** Apply `surface_container_lowest` with 80% opacity and a `backdrop-filter: blur(12px)`. This makes the UI feel integrated and premium.

---

## 3. Typography: The Editorial Voice

We utilize two distinct typefaces to balance character with functional clarity.

*   **Manrope (Display & Headlines):** A geometric sans-serif that feels modern and architectural. Use `display-lg` (3.5rem) for hero statements to establish a "luxury magazine" feel.
*   **Inter (Title, Body, Labels):** A highly legible workhorse for functional data and descriptions.

**Hierarchy Strategy:**
*   **Headlines:** Use `headline-lg` in `on_surface` for product categories.
*   **Body:** Use `body-md` (0.875rem) for descriptions to maintain a clean, airy look.
*   **Data Labels:** Use `label-md` in `on_secondary_container` for metadata (SKUs, Stock status).

---

## 4. Elevation & Depth: Tonal Layering

### The Layering Principle
Avoid "Drop Shadows" in the traditional sense. Use **Tonal Lift**:
*   A "Card" should be `surface_container_lowest` sitting on a `surface_container_low` background. The color difference *is* the border.

### Ambient Shadows
If a floating element (like a Cart Drawer) requires a shadow, use:
*   `box-shadow: 0 20px 40px rgba(25, 28, 30, 0.06);` 
*   The shadow must be tinted with the `on_surface` color, never pure black.

### The "Ghost Border" Fallback
If accessibility requires a container boundary, use a **Ghost Border**:
*   `outline_variant` at 15% opacity. It should be felt, not seen.

---

## 5. Component Guidelines

### Buttons (The Signature CTA)
*   **Primary:** Background `primary` (#0040a1), text `on_primary`. Shape: `xl` (1.5rem) for a modern, pill-like feel.
*   **Secondary:** Background `secondary_container`, text `on_secondary_container`.
*   **Interaction:** On hover, shift from `primary` to `primary_container`. No sudden color flashes; use a 300ms ease-in-out transition.

### Input Fields & Forms
*   **Style:** Minimalist. No bottom border or full-box border. Use `surface_container_high` as a solid background fill with `DEFAULT` (0.5rem) rounded corners.
*   **Focus State:** A 2px "Ghost Border" using `primary` at 40% opacity.

### Cards & Lists (The Grid Rule)
*   **Forbidden:** Divider lines (`<hr>`).
*   **Spacing:** Separate list items using `spacing-4` (1rem). 
*   **Visual Separation:** In the Admin Dashboard, use alternating background tints (`surface` vs `surface_container_low`) for large data tables instead of lines.

### Admin Sidebar (Dark Theme Context)
*   **Background:** `inverse_surface` (#2d3133).
*   **Active State:** Use a "Surface Tint" block—a vertical pill of `primary_fixed` on the left edge of the active nav item.
*   **Text:** `inverse_on_surface` for high-readability management.

---

## 6. Do's and Don'ts

### Do:
*   **Do** use `spacing-20` or `spacing-24` for hero section margins to create an "expensive" feel.
*   **Do** use `tertiary` (#822800) sparingly for "Limited Edition" or "Sale" tags to create high-end urgency.
*   **Do** apply `rounded-xl` to all product imagery to soften the professional blue palette.

### Don't:
*   **Don't** use 100% black (#000000) for text. Always use `on_surface` (#191c1e) for a softer, more natural contrast.
*   **Don't** use standard Bootstrap "Blue" (#007bff). Stick strictly to the `primary` (#0040a1) "Trustworthy Blue" for brand authority.
*   **Don't** crowd the screen. If you feel a section needs a border to separate it, it likely just needs more white space (`spacing-12`).

---

## 7. Spacing & Radius Summary
*   **Containers:** `rounded-xl` (1.5rem)
*   **Cards/Inputs:** `rounded-DEFAULT` (0.5rem)
*   **Standard Padding:** `spacing-6` (1.5rem)
*   **Section Gaps:** `spacing-16` (4rem)