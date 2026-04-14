package style

import org.jetbrains.compose.web.css.*

object AppStyles : StyleSheet() {

    val appContainer by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
        property("font-family", "'Segoe UI', system-ui, sans-serif")
        property("padding", "clamp(8px, 2vmin, 24px)")
        property("min-height", "100vh")
        property("box-sizing", "border-box")
    }

    val title by style {
        property("font-size", "clamp(20px, 4vmin, 32px)")
        property("margin", "0 0 8px 0")
        media(mediaMaxWidth(600.px)) {
            self style {
                property("font-size", "20px")
            }
        }
    }

    val board by style {
        display(DisplayStyle.Grid)
        backgroundColor(Color("#1a5276"))
        property("gap", "clamp(2px, 0.5vmin, 6px)")
        property("padding", "clamp(4px, 1vmin, 10px)")
        property("border-radius", "clamp(4px, 1vmin, 10px)")
        property("width", "clamp(280px, min(90vw, 70vh), 700px)")
        overflow("hidden")
        media(mediaMaxWidth(600.px)) {
            self style {
                property("padding", "4px")
            }
        }
    }

    val cell by style {
        property("aspect-ratio", "1")
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        justifyContent(JustifyContent.Center)
        property("cursor", "pointer")
        property("min-width", "36px")
        property("min-height", "36px")
    }

    val cellInner by style {
        width(85.percent)
        height(85.percent)
        borderRadius(50.percent)
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        justifyContent(JustifyContent.Center)
    }

    val cellRed by style {
        backgroundColor(Color("#e74c3c"))
        color(Color.white)
    }

    val cellYellow by style {
        backgroundColor(Color("#f39c12"))
        color(Color("#1a1a1a"))
    }

    val cellEmpty by style {
        backgroundColor(Color("#ecf0f1"))
    }

    val cellGlyph by style {
        property("font-size", "clamp(16px, 4vmin, 32px)")
        property("font-weight", "bold")
        property("line-height", "1")
        property("user-select", "none")
    }

    val cellGhost by style {
        property("opacity", "0.4")
    }

    val undoButton by style {
        property("min-height", "44px")
        property("padding", "10px 18px")
        property("font-size", "clamp(14px, 2.2vmin, 16px)")
        property("font-weight", "bold")
        backgroundColor(Color("#95a5a6"))
        color(Color.white)
        property("border", "none")
        borderRadius(6.px)
        property("cursor", "pointer")
        property("box-sizing", "border-box")
        media(mediaMaxWidth(600.px)) {
            self style {
                property("width", "100%")
            }
        }
    }

    val undoDisabled by style {
        property("opacity", "0.5")
        property("cursor", "not-allowed")
    }

    val pulseKeyframes by keyframes {
        0.percent { property("transform", "scale(1)") }
        50.percent { property("transform", "scale(1.08)") }
        100.percent { property("transform", "scale(1)") }
    }

    val shakeKeyframes by keyframes {
        0.percent { property("transform", "translateX(0)") }
        25.percent { property("transform", "translateX(-4px)") }
        50.percent { property("transform", "translateX(4px)") }
        75.percent { property("transform", "translateX(-4px)") }
        100.percent { property("transform", "translateX(0)") }
    }

    val cellWinning by style {
        property("box-shadow", "0 0 12px 4px rgba(255, 255, 255, 0.8)")
        border(2.px, LineStyle.Solid, Color("#fff"))
        animation(pulseKeyframes) {
            duration(1.2.s)
            timingFunction(AnimationTimingFunction.EaseInOut)
            iterationCount(null)
        }
    }

    val cellShake by style {
        animation(shakeKeyframes) {
            duration(0.4.s)
            timingFunction(AnimationTimingFunction.EaseInOut)
        }
    }

    val controlInputInvalid by style {
        border(2.px, LineStyle.Solid, Color("#e74c3c"))
        animation(shakeKeyframes) {
            duration(0.4.s)
            timingFunction(AnimationTimingFunction.EaseInOut)
        }
    }

    val controlsPanel by style {
        display(DisplayStyle.Flex)
        property("gap", "clamp(8px, 2vmin, 16px)")
        alignItems(AlignItems.Center)
        flexWrap(FlexWrap.Wrap)
        justifyContent(JustifyContent.Center)
        property("padding", "clamp(6px, 1.5vmin, 12px)")
        property("margin-bottom", "clamp(6px, 1.5vmin, 12px)")
        property("width", "100%")
        property("max-width", "700px")
        property("box-sizing", "border-box")
        media(mediaMaxWidth(600.px)) {
            self style {
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Stretch)
            }
        }
    }

    val controlGroup by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
        property("gap", "4px")
        media(mediaMaxWidth(600.px)) {
            self style {
                property("width", "100%")
            }
        }
    }

    val controlLabel by style {
        property("font-size", "clamp(12px, 1.8vmin, 14px)")
        property("font-weight", "bold")
    }

    val controlHint by style {
        property("font-size", "clamp(10px, 1.5vmin, 12px)")
        color(Color("#7f8c8d"))
    }

    val controlInput by style {
        property("width", "clamp(72px, 12vmin, 96px)")
        property("min-height", "44px")
        property("padding", "10px 18px")
        property("font-size", "clamp(14px, 2.2vmin, 16px)")
        property("text-align", "center")
        borderRadius(4.px)
        border(1.px, LineStyle.Solid, Color("#ccc"))
        property("box-sizing", "border-box")
        media(mediaMaxWidth(600.px)) {
            self style {
                property("width", "100%")
            }
        }
    }

    val newGameButton by style {
        property("min-height", "44px")
        property("padding", "10px 18px")
        property("font-size", "clamp(14px, 2.2vmin, 16px)")
        property("font-weight", "bold")
        backgroundColor(Color("#2ecc71"))
        color(Color.white)
        property("border", "none")
        borderRadius(6.px)
        property("cursor", "pointer")
        property("box-sizing", "border-box")
        media(mediaMaxWidth(600.px)) {
            self style {
                property("width", "100%")
            }
        }
    }

    val statusBar by style {
        property("font-size", "clamp(16px, 3vmin, 24px)")
        property("font-weight", "bold")
        property("padding", "8px 16px")
        property("margin-bottom", "clamp(4px, 1vmin, 8px)")
        property("text-align", "center")
        borderRadius(6.px)
    }

    val statusRed by style {
        color(Color("#e74c3c"))
    }

    val statusYellow by style {
        color(Color("#f39c12"))
    }

    val statusDraw by style {
        color(Color("#7f8c8d"))
    }

    val dropKeyframes by keyframes {
        from { property("transform", "translateY(-100%)") }
        to { property("transform", "translateY(0)") }
    }

    val cellDrop by style {
        animation(dropKeyframes) {
            duration(0.4.s)
            timingFunction(AnimationTimingFunction.EaseIn)
        }
    }

    val visuallyHidden by style {
        position(Position.Absolute)
        width(1.px)
        height(1.px)
        property("clip", "rect(0 0 0 0)")
        property("clip-path", "inset(50%)")
        overflow("hidden")
        property("white-space", "nowrap")
        property("margin", "-1px")
        property("padding", "0")
        property("border", "0")
    }

    val boardFocused by style {
        property("outline", "3px solid rgba(255, 255, 255, 0.7)")
        property("outline-offset", "2px")
    }
}
