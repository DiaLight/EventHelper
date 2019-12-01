package dialight.misc;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class Colorizer {

    public static String apply(String raw) {
        Parser parser = new Parser(raw);
        List<String> result = parser.parse();
        return result.size() > 0 ? result.get(0) : "";
    }

    public static List<String> asList(String... args) {
        Parser parser = new Parser(args);
        return parser.parse();
    }

    public static String[] apply(String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = apply(args[i]);
        }
        return args;
    }

    public static List<String> apply(List<String> lore) {
        ListIterator<String> iterator = lore.listIterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            iterator.set(apply(next));
        }
        return lore;
    }

    public static class Parser {

        private String[] inputs;

        public Parser(String input) {
            this(new String[]{input});
        }

        public Parser(Collection<String> inputs) {
            this((String[]) inputs.toArray());
        }

        public Parser(String[] inputs) {
            this.inputs = inputs;
        }

        public List<String> parse() {
            List<String> resultList = new ArrayList<>(this.inputs.length);

            for (String input : this.inputs) {
                resultList.add(parseSingle(input));
            }
            return resultList;
        }

        public String parseSingle(String raw) {
            Styles styles = new Styles();
            Colors color = Colors.NONE;

            StringBuilder result = new StringBuilder();
            for (State state : parseSingleStates(raw)) {
                if (state.styles.appendDifferent(result, styles)) {
                    state.color.append(result);
                }
                else if (color != state.color && state.color != Colors.NONE) {
                    state.color.append(result);
                }

                color = state.color;
                styles = state.styles;
                result.append(state.text);
            }
            return result.toString();
        }

        private List<State> parseSingleStates(String raw) {
            char[] chars = raw.toCharArray();
            List<State> states = new ArrayList<>();

            int currIndex = 0;
            char currChar;

            boolean skipNext = false;
            State state = new State();

            while(currIndex < raw.length()) {
                currChar = chars[currIndex++];

                if (skipNext) {
                    state.text.append(currChar);
                    skipNext = false;
                    continue;
                }

                if (currChar == '\\') {
                    skipNext = true;
                    continue;
                }

                if (currChar == '|') {

                    State newState = new State();
                    currIndex = parseStyles(newState, chars, currIndex);

                    if (state.isEmpty()) {
                        state = newState;
                        continue;
                    }

                    states.add(state);
                    state = newState;
                    continue;
                }

                state.text.append(currChar);
            }

            if (!state.isEmpty()) {
                states.add(state);
            }

            return states;
        }

        private int parseStyles(State state, char[] chars, int startIndex) {
            int currIndex = startIndex;
            char currChar;

            StringBuilder rawColor = new StringBuilder();

            while (currIndex < chars.length) {
                currChar = chars[currIndex++];

                switch (currChar) {
                    case '?':
                        state.styles.obfuscated = true;
                        break;
                    case '*':
                        state.styles.bold = true;
                        break;
                    case '~':
                        state.styles.strikethrough = true;
                        break;
                    case '_':
                        state.styles.underline = true;
                        break;
                    case '/':
                        state.styles.italic = true;
                        break;
                    case '`':
                        state.styles.reset = true;
                        break;
                    case '|':
                        setColor(state, rawColor.toString());
                        return currIndex;
                    default:
                        rawColor.append(currChar);
                        break;
                }
            }
            return chars.length;
        }

        private void setColor(State state, String name) {
            switch (name) {
                case "a":
                case "aqua":
                    state.color = Colors.AQUA;
                    break;
                case "bl":
                case "black":
                    state.color = Colors.BLACK;
                    break;
                case "b":
                case "blue":
                    state.color = Colors.BLUE;
                    break;
                case "da":
                case "dark_aqua":
                    state.color = Colors.DARK_AQUA;
                    break;
                case "db":
                case "dark_blue":
                    state.color = Colors.DARK_BLUE;
                    break;
                case "dgr":
                case "dark_gray":
                    state.color = Colors.DARK_GRAY;
                    break;
                case "dg":
                case "dark_green":
                    state.color = Colors.DARK_GREEN;
                    break;
                case "dp":
                case "dark_purple":
                    state.color = Colors.DARK_PURPLE;
                    break;
                case "dr":
                case "dark_red":
                    state.color = Colors.DARK_RED;
                    break;
                case "go":
                case "gold":
                    state.color = Colors.GOLD;
                    break;
                case "gr":
                case "gray":
                    state.color = Colors.GRAY;
                    break;
                case "g":
                case "green":
                    state.color = Colors.GREEN;
                    break;
                case "lp":
                case "light_purple":
                    state.color = Colors.LIGHT_PURPLE;
                    break;
                case "r":
                case "red":
                    state.color = Colors.RED;
                    break;
                case "w":
                case "white":
                    state.color = Colors.WHITE;
                    break;
                case "y":
                case "yellow":
                    state.color = Colors.YELLOW;
                    break;
                default:
                    state.text.insert(0, name);
                    state.color = Colors.NONE;
                    break;
            }
        }

    }

    private static class State {

        public StringBuilder text = new StringBuilder();
        public Colors color = Colors.NONE;
        public Styles styles = new Styles();

        public boolean isEmpty() {
            return text.length() < 1 && color == Colors.NONE && styles.isEmpty();
        }
    }

    public enum Colors {

        NONE(ChatColor.WHITE) {
            @Override
            public void append(StringBuilder builder) {
            }
        },

        AQUA(ChatColor.AQUA),
        BLACK(ChatColor.BLACK),
        BLUE(ChatColor.BLUE),
        DARK_AQUA(ChatColor.DARK_AQUA),
        DARK_BLUE(ChatColor.DARK_BLUE),
        DARK_GRAY(ChatColor.DARK_GRAY),
        DARK_GREEN(ChatColor.DARK_GREEN),
        DARK_PURPLE(ChatColor.DARK_PURPLE),
        DARK_RED(ChatColor.DARK_RED),
        GOLD(ChatColor.GOLD),
        GRAY(ChatColor.GRAY),
        GREEN(ChatColor.GREEN),
        LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
        RED(ChatColor.RED),
        WHITE(ChatColor.WHITE),
        YELLOW(ChatColor.YELLOW);

        private final ChatColor color;

        Colors(ChatColor color) {
            this.color = color;
        }

        public void append(StringBuilder builder) {
            builder.append(this.color);
        }

        @Override
        public String toString() {
            return this.color.toString();
        }
    }

    public static class Styles {

        public boolean reset = false;
        public boolean obfuscated = false;
        public boolean bold = false;
        public boolean strikethrough = false;
        public boolean underline = false;
        public boolean italic = false;

        public boolean hasAnyStyle() {
            return obfuscated || bold || strikethrough || underline || italic;
        }

        public boolean isEmpty() {
            return !hasAnyStyle();
        }

        public void append(StringBuilder builder) {
            if (reset) {
                builder.append(ChatColor.RESET);
            }
            if (obfuscated) {
                builder.append(ChatColor.MAGIC);
            }
            if (bold) {
                builder.append(ChatColor.BOLD);
            }
            if (strikethrough) {
                builder.append(ChatColor.STRIKETHROUGH);
            }
            if (underline) {
                builder.append(ChatColor.UNDERLINE);
            }
            if (italic) {
                builder.append(ChatColor.ITALIC);
            }
        }

        public boolean appendDifferent(StringBuilder builder, Styles styles) {
            if (styles == null || reset) {
                this.append(builder);
                return true;
            }

            if (styles.isEmpty()) {
                this.append(builder);
                return false;
            }

            if (!styles.obfuscated && obfuscated) {
                builder.append(ChatColor.MAGIC);
            }
            if (!styles.bold && bold) {
                builder.append(ChatColor.BOLD);
            }
            if (!styles.strikethrough && strikethrough) {
                builder.append(ChatColor.STRIKETHROUGH);
            }
            if (!styles.underline && underline) {
                builder.append(ChatColor.UNDERLINE);
            }
            if (!styles.italic && italic) {
                builder.append(ChatColor.ITALIC);
            }

            return false;
        }
    }

}
