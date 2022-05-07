package DT2.tidb.visitor;

import DT2.IgnoreMeException;
import DT2.Randomly;
import DT2.common.visitor.ToStringVisitor;
import DT2.tidb.TiDBBugs;
import DT2.tidb.ast.TiDBAggregate;
import DT2.tidb.ast.TiDBCase;
import DT2.tidb.ast.TiDBCastOperation;
import DT2.tidb.ast.TiDBColumnReference;
import DT2.tidb.ast.TiDBConstant;
import DT2.tidb.ast.TiDBExpression;
import DT2.tidb.ast.TiDBFunctionCall;
import DT2.tidb.ast.TiDBJoin;
import DT2.tidb.ast.TiDBJoin.JoinType;
import DT2.tidb.ast.TiDBSelect;
import DT2.tidb.ast.TiDBTableReference;
import DT2.tidb.ast.TiDBText;

public class TiDBToStringVisitor extends ToStringVisitor<TiDBExpression> implements TiDBVisitor {

    @Override
    public void visitSpecific(TiDBExpression expr) {
        TiDBVisitor.super.visit(expr);
    }

    @Override
    public void visit(TiDBConstant c) {
        sb.append(c.toString());
    }

    public String getString() {
        return sb.toString();
    }

    @Override
    public void visit(TiDBColumnReference c) {
        if (c.getColumn().getTable() == null) {
            sb.append(c.getColumn().getName());
        } else {
            sb.append(c.getColumn().getFullQualifiedName());
        }
    }

    @Override
    public void visit(TiDBTableReference expr) {
        sb.append(expr.getTable().getName());
    }

    @Override
    public void visit(TiDBSelect select) {
        sb.append("SELECT ");
        if (select.getHint() != null) {
            sb.append("/*+ ");
            visit(select.getHint());
            sb.append("*/");
        }
        visit(select.getFetchColumns());
        sb.append(" FROM ");
        visit(select.getFromList());
        if (!select.getFromList().isEmpty() && !select.getJoinList().isEmpty()) {
            sb.append(", ");
        }
        if (!select.getJoinList().isEmpty()) {
            visit(select.getJoinList());
        }
        if (select.getWhereClause() != null) {
            sb.append(" WHERE ");
            visit(select.getWhereClause());
        }
        if (!select.getGroupByExpressions().isEmpty()) {
            sb.append(" GROUP BY ");
            visit(select.getGroupByExpressions());
        }
        if (select.getHavingClause() != null) {
            sb.append(" HAVING ");
            visit(select.getHavingClause());
        }
        if (!select.getOrderByExpressions().isEmpty()) {
            sb.append(" ORDER BY ");
            visit(select.getOrderByExpressions());
        }
    }

    @Override
    public void visit(TiDBFunctionCall call) {
        sb.append(call.getFunction());
        sb.append("(");
        visit(call.getArgs());
        sb.append(")");
    }

    @Override
    public void visit(TiDBJoin join) {
        sb.append(" ");
        visit(join.getLeftTable());
        sb.append(" ");
        switch (join.getJoinType()) {
        case INNER:
            if (Randomly.getBoolean()) {
                sb.append("INNER ");
            } else {
                sb.append("CROSS ");
            }
            sb.append("JOIN ");
            break;
        case LEFT:
            sb.append("LEFT ");
            if (Randomly.getBoolean()) {
                sb.append(" OUTER ");
            }
            sb.append("JOIN ");
            break;
        case RIGHT:
            sb.append("RIGHT ");
            if (Randomly.getBoolean()) {
                sb.append(" OUTER ");
            }
            sb.append("JOIN ");
            break;
        case STRAIGHT:
            sb.append("STRAIGHT_JOIN ");
            break;
        case NATURAL:
            sb.append("NATURAL ");
            switch (join.getNaturalJoinType()) {
            case INNER:
                break;
            case LEFT:
                sb.append("LEFT ");
                break;
            case RIGHT:
                if (TiDBBugs.bug15844) {
                    throw new IgnoreMeException();
                }
                sb.append("RIGHT ");
                break;
            default:
                throw new AssertionError();
            }
            sb.append("JOIN ");
            break;
        default:
            throw new AssertionError();
        }
        visit(join.getRightTable());
        sb.append(" ");
        if (join.getJoinType() != JoinType.NATURAL) {
            sb.append("ON ");
            visit(join.getOnCondition());
        }
    }

    @Override
    public void visit(TiDBText text) {
        sb.append(text.getText());
    }

    @Override
    public void visit(TiDBAggregate aggr) {
        sb.append(aggr.getFunction());
        sb.append("(");
        visit(aggr.getArgs());
        sb.append(")");
    }

    @Override
    public void visit(TiDBCastOperation cast) {
        sb.append("CAST(");
        visit(cast.getExpr());
        sb.append(" AS ");
        sb.append(cast.getType());
        sb.append(")");
    }

    @Override
    public void visit(TiDBCase op) {
        sb.append("(CASE ");
        visit(op.getSwitchCondition());
        for (int i = 0; i < op.getConditions().size(); i++) {
            sb.append(" WHEN ");
            visit(op.getConditions().get(i));
            sb.append(" THEN ");
            visit(op.getExpressions().get(i));
        }
        if (op.getElseExpr() != null) {
            sb.append(" ELSE ");
            visit(op.getElseExpr());
        }
        sb.append(" END )");
    }
}