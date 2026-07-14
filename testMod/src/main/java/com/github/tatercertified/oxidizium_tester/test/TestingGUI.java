package com.github.tatercertified.oxidizium_tester.test;

import com.github.tatercertified.oxidizium_tester.OxidiziumTester;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class TestingGUI extends Application {
    private static boolean ran;
    private static final HashMap<String, Set<String>> FAILED_TESTS = new HashMap<>();
    private static volatile String currentTest = "";
    private static volatile String currentClass = "";
    private static volatile String currentMethod = "";
    private static final AtomicInteger currentRun = new AtomicInteger(0);
    private static final AtomicInteger TOTAL_TESTS = new AtomicInteger(0);
    private static final AtomicInteger CURRENT_TEST_INDEX = new AtomicInteger(0);
    private static final Semaphore SAFETY_LOCK = new Semaphore(1);
    private static volatile boolean benchmarkComplete = false;

    public static void setCurrentTestName(String name) {
        currentTest = name;
        accessHashMap();
        FAILED_TESTS.put(name, new HashSet<>());
        SAFETY_LOCK.release();
    }

    public static void setCurrentTestNameFast(String name) {
        currentTest = name;
    }

    public static void setCurrentClass(String className) {
        currentClass = className + ".class";
    }

    public static void setCurrentMethodFast(String method) {
        currentMethod = method;
    }

    public static void resetProgressBar() {
        CURRENT_TEST_INDEX.set(0);
        TOTAL_TESTS.set(100);
    }

    public static void setProgressBar(int i) {
        CURRENT_TEST_INDEX.set(i);
    }

    public static void setCurrentMethod(String methodAndParams) {
        currentMethod = methodAndParams;
        CURRENT_TEST_INDEX.incrementAndGet();
    }

    public static void setCurrentRun(int run) {
        currentRun.set(run);
    }

    public static void setTotalTests(int tests) {
        TOTAL_TESTS.set(tests);
    }

    public static void addError(String methodName, boolean warning, String... warningInfo) {
        accessHashMap();
        if (warning) {
            FAILED_TESTS.get(currentTest).add(methodName + ": " + warningInfo[0]);
        } else {
            FAILED_TESTS.get(currentTest).add("%RED%" + methodName + ": " + warningInfo[0]);
        }
        SAFETY_LOCK.release();
    }

    public static void reset() {
        // Ensure no CME
        accessHashMap();
        FAILED_TESTS.clear();
        SAFETY_LOCK.release();
        TOTAL_TESTS.set(0);
        currentRun.set(0);
        CURRENT_TEST_INDEX.set(0);
        currentClass = "";
        currentMethod = "";
        currentTest = "";
        benchmarkComplete = false;
    }

    public static void setBenchmarkComplete(boolean complete) {
        benchmarkComplete = complete;
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Oxidizium Native Testing");
        config.setHeight(600);
        config.setWidth(600);
        super.configure(config);
    }

    @Override
    protected void preRun() {
        OxidiziumTester.TEST_LOGGER.info("Starting Native Testing");
        super.preRun();
    }

    @Override
    protected void initWindow(Configuration config) {
        super.initWindow(config);
    }

    @Override
    protected void disposeWindow() {
        super.disposeWindow();
        System.exit(0);
    }

    @Override
    public void process() {
        // Set position and size before Begin
        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(600, 600);

        int windowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar;

        ImGui.begin("Oxidizium Native Tester", windowFlags);

        // Padding and layout constants
        float padding = 20.0f;
        float buttonHeight = 30.0f;
        float progressBarHeight = 20.0f;
        float spacing = 10.0f;

        float windowHeight = ImGui.getWindowSizeY();
        float windowWidth = ImGui.getWindowSizeX();

        // Box dimensions
        float boxWidth = 250.0f;
        float boxHeight = 400.0f;

        // ---------------- Left Box (Errors) ----------------

        ImGui.setCursorPosX(padding + boxWidth / 2 - ImGui.calcTextSizeX("Errors") / 2);
        ImGui.setCursorPosY(padding);
        ImGui.text("Errors");

        ImGui.setCursorPosX(padding);
        ImGui.setCursorPosY(padding * 3);

        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0xFF2E2E2E); // dark gray background
        ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, ImColor.rgb(46, 46, 46)); // dark gray background
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, ImColor.rgb(239, 121, 0)); // Ferris orange
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, ImColor.rgb(255, 150, 50)); // lighter orange
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, ImColor.rgb(189, 71, 0)); // deeper orange

        ImGui.beginChild("LogBox", boxWidth, boxHeight, true, ImGuiWindowFlags.HorizontalScrollbar);
        // Ensure no CME
        accessHashMap();
        for (Map.Entry<String, Set<String>> testFails : FAILED_TESTS.entrySet()) {

            ImGui.pushStyleColor(ImGuiCol.Header, ImColor.rgb(239, 121, 0));
            ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImColor.rgb(255, 140, 20));
            ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImColor.rgb(255, 120, 0));
            if (!testFails.getValue().isEmpty() && ImGui.collapsingHeader(testFails.getKey())) {
                for (String line : testFails.getValue()) {
                    if (line.contains("%RED%")) {
                        ImGui.textColored(ImColor.rgb(255, 0, 0), line.replace("%RED%", ""));
                    } else {
                        ImGui.text(line);
                    }
                }
            }
            ImGui.popStyleColor(3);
        }
        SAFETY_LOCK.release();
        ImGui.endChild();

        ImGui.popStyleColor(5);

        // ---------------- Right Box (Test Info) ----------------

        float rightBoxX = windowWidth - padding - boxWidth;

        ImGui.setCursorPosX(rightBoxX + boxWidth / 2 - ImGui.calcTextSizeX("Info") / 2);
        ImGui.setCursorPosY(padding);
        ImGui.text("Info");

        ImGui.setCursorPosX(rightBoxX);
        ImGui.setCursorPosY(padding * 3);

        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0xFF2E2E2E); // same gray
        ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, ImColor.rgb(46, 46, 46)); // dark gray background
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, ImColor.rgb(239, 121, 0)); // Ferris orange
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, ImColor.rgb(255, 150, 50)); // lighter orange
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, ImColor.rgb(189, 71, 0)); // deeper orange

        ImGui.beginChild("InfoBox", boxWidth, boxHeight, true, ImGuiWindowFlags.HorizontalScrollbar);

        if (benchmarkComplete) {
            renderBenchmarkResults(boxWidth);
        } else {
            centerText("Test Name", boxWidth, true);
            multiSpace(4);
            centerText(currentTest, boxWidth, false);
            multiSpace(6);
            centerText("Class", boxWidth, true);
            multiSpace(4);
            centerText(currentClass, boxWidth, false);
            multiSpace(6);
            centerText("Method", boxWidth, true);
            multiSpace(4);
            centerText(currentMethod, boxWidth, false);
            multiSpace(6);
            centerText("Runs Per Test", boxWidth, true);
            multiSpace(4);
            center(boxWidth - padding * 2, boxWidth);
            ImGui.setNextItemWidth(boxWidth - padding * 2);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, ImColor.rgb(239, 121, 0));
            ImGui.inputInt("##Runs", NativeTest.getRunsPerTest(), -1);
            ImGui.popStyleColor();
            multiSpace(14);
            centerText("Run: " + (currentRun.get() + 1), boxWidth, false);
        }

        ImGui.endChild();

        ImGui.popStyleColor(5);

        // ---------------- Bottom Button ----------------
        // Shared between Progress Bar and Button
        float progress = CURRENT_TEST_INDEX.floatValue() / TOTAL_TESTS.floatValue();

        float totalHeight = progressBarHeight + spacing + buttonHeight + 2 * padding;
        float startY = windowHeight - totalHeight;

        ImGui.setCursorPosY(startY + padding);

        float buttonWidth = 150.0f;
        ImGui.setCursorPosX((windowWidth - buttonWidth) / 2);
        ImGui.pushStyleColor(ImGuiCol.Button, ImColor.rgb(239, 121, 0));
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, ImColor.rgb(255, 140, 20));
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, ImColor.rgb(255, 120, 0));
        boolean testsComplete = progress == 1.0f;
        String buttonLabel;
        if (testsComplete) {
            buttonLabel = "Retest";
            ran = false;
        } else {
            buttonLabel = "Start Tests";
        }
        if (!ran && ImGui.button(buttonLabel, buttonWidth, buttonHeight)) {
            new Thread(NativeTest::invokeTests).start();
            ran = true;
        }
        ImGui.popStyleColor(3);

        // ---------------- Progress Bar ----------------
        ImGui.setCursorPosY(ImGui.getCursorPosY() + spacing);


        float progressBarWidth = 300.0f;
        ImGui.setCursorPosX((windowWidth - progressBarWidth) / 2);

        float barPosX = ImGui.getCursorScreenPosX();
        float barPosY = ImGui.getCursorScreenPosY();

        ImGui.pushStyleColor(ImGuiCol.PlotHistogram, ImColor.rgb(255, 140, 20));
        ImGui.pushStyleColor(ImGuiCol.FrameBg, ImColor.rgb(189, 71, 0));
        ImGui.progressBar(progress, progressBarWidth, progressBarHeight, "");
        ImGui.popStyleColor(2);

        String percentText = String.format("%.0f%%", progress * 100);
        float textWidth = ImGui.calcTextSizeX(percentText);
        float textHeight = ImGui.getFontSize();
        float textPosX = barPosX + (progressBarWidth - textWidth) / 2;
        float textPosY = barPosY + (progressBarHeight - textHeight) / 2;
        ImGui.getWindowDrawList().addText(textPosX, textPosY, ImGui.getColorU32(ImGuiCol.Text), percentText);

        ImGui.end();
    }

    private static void accessHashMap() {
        try {
            SAFETY_LOCK.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void centerText(String text, float windowWidth, boolean underline) {
        ImGui.setCursorPosX((windowWidth - ImGui.calcTextSizeX(text)) / 2);
        if (underline) {
            underlinedText(text);
        } else {
            ImGui.text(text);
        }
    }

    private static void center(float objWidth, float windowWidth) {
        ImGui.setCursorPosX((windowWidth - objWidth) / 2);
    }

    private static void underlinedText(String text) {
        ImGui.text(text);

        // Get draw list and current cursor position
        ImDrawList drawList = ImGui.getWindowDrawList();
        float textWidth = ImGui.calcTextSizeX(text);
        float textHeight = ImGui.getFontSize();

        // Get top-left corner of the text
        float textStartX = ImGui.getItemRectMinX();
        float textStartY = ImGui.getItemRectMinY();

        // Underline 1px below the text
        float underlineY = textStartY + textHeight;

        // Draw the underline
        drawList.addLine(
                textStartX, underlineY,
                textStartX + textWidth, underlineY,
                ImGui.getColorU32(ImGuiCol.Text) // Match text color
        );
    }

    private static void multiSpace(int times) {
        for (int i = 0; i < times; i++) {
            ImGui.spacing();
        }
    }

    /**
     * Renders benchmark results in the Info panel.
     * Methods where native is slower than Java are highlighted in red.
     * When either time is at JMH's measurement floor (~10^-3), raw times
     * are shown instead of a ratio (which would be meaningless from noisy data).
     */
    private static void renderBenchmarkResults(float boxWidth) {
        List<BenchmarkResult> results = BenchmarkManager.getResults();

        centerText("Benchmark Results", boxWidth, true);
        multiSpace(2);

        // Summary line
        long slowerCount = BenchmarkManager.getSlower();
        String summary = String.format("%d methods | %d native slower", results.size(), slowerCount);
        if (slowerCount > 0) {
            ImGui.textColored(ImColor.rgb(255, 80, 80), summary);
        } else {
            ImGui.text(summary);
        }
        multiSpace(2);

        // Column headers
        centerText("Ratio", boxWidth, false);
        ImGui.sameLine();
        ImGui.setCursorPosX(boxWidth - 60);
        centerText("Method", 60, false);
        multiSpace(1);

        // Separator line
        float sepX1 = 5;
        float sepX2 = boxWidth - 5;
        float sepY = ImGui.getItemRectMaxY();
        ImDrawList drawList = ImGui.getWindowDrawList();
        drawList.addLine(sepX1, sepY, sepX2, sepY, ImGui.getColorU32(ImGuiCol.TextDisabled));
        multiSpace(1);

        // Results (sorted: slower-native first)
        for (BenchmarkResult result : results) {
            String methodLabel = result.getMethodName();
            double ratio = result.speedImprovement();

            String ratioText;
            if (ratio <= 0) {
                ratioText = "N/A";
            } else {
                ratioText = String.format("%.2fx", ratio);
            }

            // Truncate long method names
            if (methodLabel.length() > 25) {
                methodLabel = methodLabel.substring(0, 22) + "...";
            }

            if (ratio > 0 && ratio < 1.0) {
                // RED highlighting for native slower than Java
                ImGui.textColored(ImColor.rgb(255, 60, 60), methodLabel);
                ImGui.sameLine();
                ImGui.setCursorPosX(boxWidth - 60);
                ImGui.textColored(ImColor.rgb(255, 60, 60), ratioText);
            } else {
                ImGui.text(methodLabel);
                ImGui.sameLine();
                ImGui.setCursorPosX(boxWidth - 60);
                ImGui.text(ratioText);
            }
            ImGui.spacing();
        }

        multiSpace(2);
        centerText(String.format("Seconds per Iteration: %d", NativeTest.getBenchmarkTime()), boxWidth, false);
        multiSpace(1);
        centerText("(ratio = Java time / Native time)", boxWidth, false);
        centerText(">1.0 = native faster, <1.0 = native slower", boxWidth, false);
    }
}
